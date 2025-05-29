package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.aoclient.engine.game.models.Key;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Gestiona las entradas del teclado en una ventana GLFW y administra el estado de las teclas.
 * <p>
 * Esta clase unifica la funcionalidad de escucha de eventos de teclado (anteriormente en KeyListener) y la gestion de mapeo de
 * teclas (anteriormente en KeyManager).
 * <p>
 * Los arreglos KEY_JUST_PRESSED y KEY_JUST_RELEASED permiten distinguir entre "mantener presionada una tecla" y "presionar una
 * tecla una sola vez". Esto es esencial para acciones que deben ocurrir solo una vez al presionar una tecla (como abrir un menu)
 * versus acciones continuas mientras se mantiene presionada (como moverse). A diferencia del sistema anterior que usaba
 * isKeyReadyForAction() y modificaba el estado al consultarlo (lo que causaba comportamientos inconsistentes si varias partes del
 * codigo verificaban la misma tecla), este nuevo enfoque separa la consulta de la actualizacion, permitiendo verificar el estado
 * de una tecla multiples veces sin alterarlo. Ademas, al actualizar los estados de manera centralizada mediante el metodo
 * update(), el codigo se vuelve mas predecible y robusto, proporcionando informacion mas completa sobre si una tecla esta siendo
 * presionada continuamente, si acaba de ser presionada o si acaba de ser liberada.
 * <p>
 * Proporciona metodos para:
 * <ul>
 * <li>Detectar eventos de teclas (presionadas, soltadas, recien presionadas, recien soltadas)
 * <li>Administrar el estado de las teclas de movimiento, permitiendo multiples teclas simultaneas
 * <li>Integrar con ImGui para la interfaz grafica
 * </ul>
 */

public enum KeyHandler {

    INSTANCE;

    /** Lista de teclas de movimiento actualmente presionadas. */
    private static final List<Integer> movementKeysPressed = new ArrayList<>();
    /** Objeto para gestionar la integracion ImGui y GLFW. */
    private static final ImGuiIO imGuiIo = ImGui.getIO();
    /** Array que almacena el estado de las teclas actualmente presionadas. */
    private static final boolean[] keyPressed = new boolean[350]; // Sin keyPressed, el personaje solo se moveria un pixel por cada pulsacion de tecla en lugar de moverse suavemente
    /** Array que almacena las teclas que fueron recien presionadas en el ultimo frame. */
    private static final boolean[] keyJustPressed = new boolean[350]; // Sin keyJustPresse, las accion como ocultarse, se ejecutarian 60 veces por segundo mientras mantienes presionado la O
    /** Array que almacena las teclas que fueron recien liberadas en el ultimo frame. */
    private static final boolean[] keyJustReleased = new boolean[350];
    /** Almacena el codigo de la ultima tecla presionada. */
    private static int lastKeyPressed;
    /** Almacena el codigo de la ultima tecla de movimiento presionada. */
    private static int lastMovementKeyPressed;

    /**
     * Gestiona los eventos del teclado capturados en la ventana GLFW y actualiza el estado de las teclas, incluyendo
     * modificadores, teclas presionadas, liberadas y las teclas de movimiento.
     *
     * @param window   identificador de la ventana en la que ocurrio el evento
     * @param key      codigo de la tecla que genero el evento
     * @param scancode codigo escaneado hardware-specific de la tecla
     * @param action   accion realizada sobre la tecla (ej. presionada o liberada)
     * @param mods     combinacion de teclas modificadoras activas (como Shift, Ctrl, Alt)
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        // Ignora eventos que no son PRESS ni RELEASE
        if (action != GLFW_PRESS && action != GLFW_RELEASE) return;

        boolean isPressed = action == GLFW_PRESS;

        // Actualiza flags de teclas recien presionadas/liberadas
        if (isPressed) {
            if (!keyPressed[key]) keyJustPressed[key] = true;
            lastKeyPressed = key;
            // Gestion de teclas de movimiento al presionar
            if (isMovementKey(key)) {
                lastMovementKeyPressed = key;
                movementKeysPressed.add(key);
            }
        } else {
            if (keyPressed[key]) keyJustReleased[key] = true;
            // Gestion de teclas de movimiento al liberar
            if (isMovementKey(key)) movementKeysPressed.remove(Integer.valueOf(key));
        }

        // Actualiza estado actual
        keyPressed[key] = isPressed;
        imGuiIo.setKeysDown(key, isPressed);

        updateModifierKeys();

    }

    /**
     * Actualiza el estado de las teclas al final de cada frame.
     * <p>
     * Este metodo debe ser llamado al final de cada frame para limpiar los estados temporales (justPressed/justReleased) y
     * mantener actualizados los estados persistentes.
     * <p>
     * En terminos de rendimiento, el impacto de actualizar el estado de las teclas es practicamente imperceptible en cualquier
     * hardware moderno. Si realizamos un analisis cuantitativo, asumiendo conservadoramente que cada asignacion booleana consume
     * aproximadamente 1 nanosegundo en una CPU estandar (aunque probablemente sea menos en procesadores actuales), y considerando
     * que el metodo update() realiza 350 × 2 asignaciones para reiniciar los arreglos KEY_JUST_PRESSED y KEY_JUST_RELEASED,
     * estariamos hablando de un costo total de apenas 700 nanosegundos por cada frame del juego. Para contextualizar esta cifra,
     * en un juego que se ejecuta a 60 fotogramas por segundo, cada frame dispone de aproximadamente 16.67 milisegundos para
     * completar todas sus operaciones, lo que significa que esta actualizacion de estados consume solamente el 0.004% del tiempo
     * disponible por frame.
     */
    public static void update() {
        for (int i = 0; i < keyJustPressed.length; i++) {
            keyJustPressed[i] = false;
            keyJustReleased[i] = false;
        }
    }

    /**
     * Verifica si la tecla especificada esta siendo presionada actualmente.
     *
     * @param keyCode codigo de la tecla que se desea verificar
     * @return {@code true} si la tecla esta presionada, en caso contrario {@code false}
     */
    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    /**
     * Verifica si la tecla especificada acaba de ser presionada en este frame.
     *
     * @param keyCode codigo de la tecla que se desea verificar
     * @return {@code true} si la tecla acaba de ser presionada, en caso contrario {@code false}
     */
    public static boolean isKeyJustPressed(int keyCode) {
        return keyJustPressed[keyCode];
    }

    /**
     * Verifica si la tecla especificada acaba de ser liberada en este frame.
     *
     * @param keyCode codigo de la tecla que se desea verificar
     * @return {@code true} si la tecla acaba de ser liberada, en caso contrario {@code false}
     */
    public static boolean isKeyJustReleased(int keyCode) {
        return keyJustReleased[keyCode];
    }

    /**
     * Verifica si la tecla de una accion especifica esta siendo presionada.
     *
     * @param key la tecla de accion que se desea verificar
     * @return {@code true} si la tecla esta presionada, en caso contrario {@code false}
     */
    public static boolean isActionKeyPressed(Key key) {
        return isKeyPressed(key.getKeyCode());
    }

    /**
     * Verifica si la tecla de una accion especifica acaba de ser presionada en este frame.
     *
     * @param key la tecla de accion que se desea verificar
     * @return {@code true} si la tecla acaba de ser presionada, en caso contrario {@code false}
     */
    public static boolean isActionKeyJustPressed(Key key) {
        return isKeyJustPressed(key.getKeyCode());
    }

    /**
     * Verifica si la tecla de una accion especifica acaba de ser liberada en este frame.
     *
     * @param key la tecla de accion que se desea verificar
     * @return {@code true} si la tecla acaba de ser liberada, en caso contrario {@code false}
     */
    public static boolean isActionKeyJustReleased(Key key) {
        return isKeyJustReleased(key.getKeyCode());
    }

    /**
     * Obtiene la ultima tecla presionada por el usuario.
     */
    public static int getLastKeyPressed() {
        return lastKeyPressed;
    }

    /**
     * Establece manualmente la ultima tecla presionada.
     */
    public static void setLastKeyPressed(int keyCode) {
        lastKeyPressed = keyCode;
    }

    /**
     * Obtiene la ultima tecla de movimiento presionada por el usuario.
     */
    public static int getLastMovementKeyPressed() {
        return lastMovementKeyPressed;
    }

    /**
     * Obtiene una copia de la lista de codigos de teclas de movimiento presionadas.
     * <p>
     * Se devuelve una copia de la lista original para mantener la integridad de los datos y evitar que se modifique la lista
     * desde codigo externo, ya que la lista {@code movementKeysPressed} es un estado interno critico del sistema de manejo de
     * teclas. Por lo tanto, al crear una copia, se mantiene el control total sobre el estado interno de la clase, permitiendo que
     * solo los metodos internos de {@code KeyHandler} modifiquen la lista original.
     *
     * @return una copia de la lista de codigos de teclas de movimiento presionadas
     */
    public static List<Integer> getMovementKeysPressed() {
        return new ArrayList<>(movementKeysPressed);
    }

    /**
     * Verifica si hay alguna tecla de movimiento presionada.
     *
     * @return {@code true} si hay alguna tecla de movimiento presionada
     */
    public static boolean isAnyMovementKeyPressed() {
        return !movementKeysPressed.isEmpty();
    }

    /**
     * Actualiza el estado de las teclas modificadoras para ImGui.
     */
    private static void updateModifierKeys() {
        imGuiIo.setKeyCtrl(imGuiIo.getKeysDown(GLFW_KEY_LEFT_CONTROL) || imGuiIo.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
        imGuiIo.setKeyShift(imGuiIo.getKeysDown(GLFW_KEY_LEFT_SHIFT) || imGuiIo.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
        imGuiIo.setKeyAlt(imGuiIo.getKeysDown(GLFW_KEY_LEFT_ALT) || imGuiIo.getKeysDown(GLFW_KEY_RIGHT_ALT));
        imGuiIo.setKeySuper(imGuiIo.getKeysDown(GLFW_KEY_LEFT_SUPER) || imGuiIo.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    }

    /**
     * Verifica si la tecla especificada es una tecla de movimiento.
     *
     * @param keyCode codigo de la tecla a verificar
     * @return {@code true} si es una tecla de movimiento
     */
    private static boolean isMovementKey(int keyCode) {
        return keyCode == Key.UP.getKeyCode() || keyCode == Key.DOWN.getKeyCode() || keyCode == Key.LEFT.getKeyCode() || keyCode == Key.RIGHT.getKeyCode();
    }

}