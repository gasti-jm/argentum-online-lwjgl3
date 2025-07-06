package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.aoclient.engine.game.models.Key;

import java.util.Arrays;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Gestiona las entradas del teclado en una ventana GLFW y administra el estado de las teclas.
 */

public enum KeyHandler {

    INSTANCE;

    private static final ImGuiIO imGuiIo = ImGui.getIO();
    /** Arreglo que almacena el estado de presionado (true) o no presionado (false) para cada tecla hasta el indice 350. */
    private static final boolean[] keyPressed = new boolean[350];
    /** Arreglo que indica cuando una tecla ha sido presionada por primera vez, evitando el efecto de repeticion de teclas. */
    private static final boolean[] keyJustPressed = new boolean[350];
    /** Arreglo que indica cuando una tecla ha sido liberada por primera vez, evitando el efecto de repeticion de teclas. */
    private static final boolean[] keyJustReleased = new boolean[350];

    /** Conjunto de teclas asociadas al movimiento del personaje (arriba, abajo, izquierda, derecha). */
    private static Set<Integer> MOVEMENT_KEYS = Set.of(Key.UP.getKeyCode(), Key.DOWN.getKeyCode(), Key.LEFT.getKeyCode(), Key.RIGHT.getKeyCode());

    private static int baseMovementKey = -1;
    private static int temporaryMovementKey = -1;
    private static int lastKeyPressed = -1;
    private static int lastMovementKeyPressed = -1;

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action != GLFW_PRESS && action != GLFW_RELEASE) return;
        boolean isPressed = action == GLFW_PRESS;
        // Actualiza flags y rastrea ultima tecla
        updateKeyStates(key, isPressed);
        // Manejo optimizado de teclas de movimiento
        if (MOVEMENT_KEYS.contains(key)) handleMovementKey(key, isPressed);
        updateModifierKeys();
    }

    public static int getLastKeyPressed() {
        return lastKeyPressed;
    }

    public static int getLastMovementKeyPressed() {
        return lastMovementKeyPressed;
    }

    /**
     * Retorna la tecla de movimiento efectiva basada en el estado actual del movimiento. Si el movimiento temporal esta activo,
     * retorna la tecla temporal; de lo contrario, retorna la tecla base de movimiento.
     *
     * @return El codigo de la tecla de movimiento efectiva. Puede ser la tecla base o la temporal, dependiendo de si el
     * movimiento temporal esta activo.
     */
    public static int getEffectiveMovementKey() {
        return isTemporaryMovementActive() ? temporaryMovementKey : baseMovementKey;
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public static boolean isKeyJustPressed(int keyCode) {
        return keyJustPressed[keyCode];
    }

    public static boolean isKeyJustReleased(int keyCode) {
        return keyJustReleased[keyCode];
    }

    public static boolean isActionKeyPressed(Key key) {
        return keyPressed[key.getKeyCode()];
    }

    public static boolean isActionKeyJustPressed(Key key) {
        return keyJustPressed[key.getKeyCode()];
    }

    public static boolean isActionKeyJustReleased(Key key) {
        return keyJustReleased[key.getKeyCode()];
    }

    /**
     * Actualiza los estados de las teclas en el sistema, marcando como no activas las banderas relacionadas con eventos de "tecla
     * recien presionada" y "tecla recien liberada". Adicionalmente, se asegura de validar y sincronizar las teclas de movimiento
     * activas.
     */
    public static void update() {
        Arrays.fill(keyJustPressed, false);
        Arrays.fill(keyJustReleased, false);
        validateMovementKeys();
    }

    /**
     * Actualiza el estado de una tecla especifica, marcando si ha sido presionada o liberada, y gestionando las banderas
     * relacionadas con eventos como "tecla recien presionada" o "tecla recien liberada". Tambien sincroniza el estado de la tecla
     * con ImGui.
     *
     * @param key       codigo de la tecla cuya informacion debe ser actualizada
     * @param isPressed indica si la tecla esta presionada (true) o no (false).
     */
    private static void updateKeyStates(int key, boolean isPressed) {
        if (isPressed && !keyPressed[key]) {
            keyJustPressed[key] = true;
            lastKeyPressed = key;

            // hay una tecla para bindear?
            if(Key.checkIsBinding()) {
                Key keyToBind = Key.getKeyBinding(); // dame la key a bindear

                if(keyToBind != null) {
                    keyToBind.setKeyCode(key); // bindeala!
                    keyToBind.setPreparedToBind(false); // no hace falta seguir bindeando.

                    // reseteamos por si se modifico las teclas de movimiento.
                    defaultMovementKeys();
                }
            }

        } else if (!isPressed && keyPressed[key]) keyJustReleased[key] = true;
        keyPressed[key] = isPressed;
        imGuiIo.setKeysDown(key, isPressed);
    }

    public static void defaultMovementKeys() {
        MOVEMENT_KEYS = Set.of(Key.UP.getKeyCode(), Key.DOWN.getKeyCode(), Key.LEFT.getKeyCode(), Key.RIGHT.getKeyCode());
    }

    /**
     * Gestiona la presion o liberacion de una tecla de movimiento. Actualiza las variables internas que reflejan las teclas de
     * movimiento activas, diferenciando entre los estados de presion y liberacion. Si la tecla es presionada, se considera como
     * ultima tecla de movimiento registrada y se procesa su activacion; si se libera, se realiza el manejo correspondiente para
     * actualizar el estado.
     *
     * @param key       codigo de la tecla que se esta gestionando
     * @param isPressed indica si la tecla esta siendo presionada (true) o liberada (false)
     */
    private static void handleMovementKey(int key, boolean isPressed) {
        if (isPressed) {
            lastMovementKeyPressed = key;
            handleMovementPress(key);
        } else handleMovementRelease(key);
    }

    /**
     * Gestiona la accion de presionar una tecla de movimiento. Actualiza las variables internas para reflejar el estado de las
     * teclas base y temporal de movimiento, asegurando la coherencia en casos donde las teclas oprimidas entran en conflicto (por
     * ejemplo, direcciones opuestas).
     *
     * @param key codigo de la tecla que se ha presionado
     */
    private static void handleMovementPress(int key) {
        if (baseMovementKey == -1) baseMovementKey = key;
        else if (baseMovementKey != key) {
            if (isOpposite(baseMovementKey, key)) temporaryMovementKey = key;
            else {
                baseMovementKey = key;
                temporaryMovementKey = -1;
            }
        }
    }

    /**
     * Gestiona la liberacion de una tecla de movimiento y actualiza las variables internas de las teclas base y temporal. Si la
     * tecla liberada coincide con {@code temporaryMovementKey}, se restablece su valor a {@code -1}. Si la tecla liberada
     * coincide con {@code baseMovementKey}, se reasigna la tecla base utilizando {@code temporaryMovementKey} si este es valido,
     * o invocando {@link #findNewBaseKey()} como alternativa.
     *
     * @param key codigo de la tecla que ha sido liberada
     */
    private static void handleMovementRelease(int key) {
        if (key == temporaryMovementKey) temporaryMovementKey = -1;
        else if (key == baseMovementKey) {
            baseMovementKey = (temporaryMovementKey != -1) ? temporaryMovementKey : findNewBaseKey();
            temporaryMovementKey = -1;
        }
    }

    /**
     * Valida y actualiza las teclas de movimiento actualmente activas, asegurando consistencia entre el estado de las teclas y
     * las variables correspondientes.
     * <ul>
     * <li>Si la tecla de movimiento base ({@code baseMovementKey}) ya no esta activa o no esta presionada, se asigna una nueva
     * tecla base utilizando el metodo {@link #findNewBaseKey()}.
     * <li>Si la tecla de movimiento temporal ({@code temporaryMovementKey}) deja de estar presionada, se restablece su valor a
     * -1, indicando la ausencia de una tecla temporal activa.
     * </ul>
     * Esta validacion es esencial para gestionar correctamente la persistencia de las teclas de movimiento en un contexto que
     * permite multiples entradas.
     */
    private static void validateMovementKeys() {
        if (baseMovementKey != -1 && !keyPressed[baseMovementKey]) baseMovementKey = findNewBaseKey();
        if (temporaryMovementKey != -1 && !keyPressed[temporaryMovementKey]) temporaryMovementKey = -1;
    }

    /**
     * Busca y retorna el codigo de la primera tecla de movimiento activa, evaluando las teclas definidas en
     * {@code MOVEMENT_KEYS}. Una tecla se considera activa si su estado actual en el arreglo {@code keyPressed} es {@code true}.
     *
     * @return el codigo de la primera tecla de movimiento activa, o {@code -1} si ninguna tecla de movimiento esta activa
     */
    private static int findNewBaseKey() {
        return MOVEMENT_KEYS.stream()
                .filter(keyCode -> keyPressed[keyCode])
                .findFirst()
                .orElse(-1);
    }

    /**
     * Determina si el movimiento temporal esta activo evaluando el estado de las teclas asociadas al movimiento base y movimiento
     * temporal. Esta activo cuando ambas teclas, la de movimiento temporal y la de movimiento base, estan presionadas, y ademas
     * la tecla de movimiento temporal no tiene un valor predeterminado (-1).
     *
     * @return {@code true} si el movimiento temporal esta activo; {@code false} en caso contrario
     */
    private static boolean isTemporaryMovementActive() {
        return temporaryMovementKey != -1 && keyPressed[temporaryMovementKey] && keyPressed[baseMovementKey];
    }

    /**
     * Determina si dos teclas representan direcciones opuestas.
     *
     * @param key1 codigo de la primera tecla
     * @param key2 codigo de la segunda tecla
     * @return {@code true} si las teclas representan direcciones opuestas; {@code false} en caso contrario
     */
    private static boolean isOpposite(int key1, int key2) {
        int up = Key.UP.getKeyCode(), down = Key.DOWN.getKeyCode();
        int left = Key.LEFT.getKeyCode(), right = Key.RIGHT.getKeyCode();
        return (key1 == up && key2 == down) || (key1 == down && key2 == up) || (key1 == left && key2 == right) || (key1 == right && key2 == left);
    }

    private static void updateModifierKeys() {
        imGuiIo.setKeyCtrl(isModifierPressed(GLFW_KEY_LEFT_CONTROL, GLFW_KEY_RIGHT_CONTROL));
        imGuiIo.setKeyShift(isModifierPressed(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT));
        imGuiIo.setKeyAlt(isModifierPressed(GLFW_KEY_LEFT_ALT, GLFW_KEY_RIGHT_ALT));
        imGuiIo.setKeySuper(isModifierPressed(GLFW_KEY_LEFT_SUPER, GLFW_KEY_RIGHT_SUPER));
    }

    private static boolean isModifierPressed(int leftKey, int rightKey) {
        return imGuiIo.getKeysDown(leftKey) || imGuiIo.getKeysDown(rightKey);
    }


}