package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.aoclient.engine.game.KeyManager;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.game.models.Key.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Gestiona las entradas del teclado en una ventana GLFW. Proporciona funciones para detectar eventos de teclas, conocer estados
 * actuales y gestionar la asignacion de teclas a acciones especificas utilizando el binding definido en la clase
 * {@code BindKeys}.
 * <p>
 * Esta clase actua como listener para los eventos del teclado, permitiendo manejar acciones realizadas por el usuario y
 * traducirlas a las acciones vinculadas en el contexto del.
 */

public enum KeyListener {

    INSTANCE;

    /** Lista de teclas recientemente presionadas para movimiento. */
    public static final List<Integer> LAST_KEYS_MOVED_PRESSED = new ArrayList<>();
    /** Objeto para gestionar la integracion ImGui y GLFW, manejando eventos y estados de entrada y salida. */
    private static final ImGuiIO IM_GUI_IO = ImGui.getIO();
    /** Referencia a la instancia unica del administrador de teclas. */
    private static final KeyManager KEY_MANAGER = KeyManager.INSTANCE;
    /** Array que almacena el estado actual de cada tecla (true si esta presionada, false si no). */
    private static final boolean[] KEY_PRESSED = new boolean[350];
    /** Almacena el codigo de la ultima tecla presionada. */
    private static int lastKeyPressed;
    /** Almacena el codigo de la ultima tecla de direccion presionada. */
    private static int lastKeyMovedPressed;

    /**
     * Funcion callBack para gestionar el listener de teclas de la ventana GLFW.
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            KEY_PRESSED[key] = true;
            IM_GUI_IO.setKeysDown(key, true);

            lastKeyPressed = key;

            if (key == KEY_MANAGER.getKeyCode(UP) || key == KEY_MANAGER.getKeyCode(LEFT) ||
                    key == KEY_MANAGER.getKeyCode(DOWN) || key == KEY_MANAGER.getKeyCode(RIGHT)) {
                lastKeyMovedPressed = key;
                LAST_KEYS_MOVED_PRESSED.add(key);
            }

        } else if (action == GLFW_RELEASE) {
            KEY_PRESSED[key] = false;
            IM_GUI_IO.setKeysDown(key, false);
            if (key == KEY_MANAGER.getKeyCode(UP) || key == KEY_MANAGER.getKeyCode(LEFT) ||
                    key == KEY_MANAGER.getKeyCode(DOWN) || key == KEY_MANAGER.getKeyCode(RIGHT)) {
                LAST_KEYS_MOVED_PRESSED.remove(LAST_KEYS_MOVED_PRESSED.indexOf(key));
            }
        }

        IM_GUI_IO.setKeyCtrl(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_CONTROL) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
        IM_GUI_IO.setKeyShift(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_SHIFT) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
        IM_GUI_IO.setKeyAlt(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_ALT) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_ALT));
        IM_GUI_IO.setKeySuper(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_SUPER) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    }

    /**
     * Verifica si la tecla especificada esta siendo presionada.
     *
     * @param keyCode codigo de la tecla que se desea verificar
     * @return {@code true} si la tecla esta presionada, en caso contrario {@code false}
     */
    public static boolean isKeyPressed(int keyCode) {
        return KEY_PRESSED[keyCode];
    }

    /**
     * Verifica si una tecla especificada esta lista para realizar una accion. Esto incluye verificar si la tecla fue presionada
     * y, en caso afirmativo, limpiar su estado para evitar acciones repetidas en el mismo ciclo.
     *
     * @param keyCode codigo de la tecla que se desea verificar
     * @return {@code true} si la tecla estaba presionada y lista para realizar una accion, en caso contrario {@code false}
     */
    public static boolean isKeyReadyForAction(int keyCode) {
        // Si la tecla estaba presionada en un momento anterior
        boolean wasKeyPressed = KEY_PRESSED[keyCode];
        if (wasKeyPressed) KEY_PRESSED[keyCode] = false;
        return wasKeyPressed;
    }

    /**
     * Devulve la ultima tecla presionada por el usuario.
     */
    public static int getLastKeyPressed() {
        return lastKeyPressed;
    }

    public static void setLastKeyPressed(int keyCode) {
        lastKeyPressed = keyCode;
    }

    public static int getLastKeyMovedPressed() {
        return lastKeyMovedPressed;
    }

    public static void setLastKeyMovedPressed(int keyCode) {
        lastKeyMovedPressed = keyCode;
    }

}

