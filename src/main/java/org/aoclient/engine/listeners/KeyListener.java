package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.aoclient.engine.game.BindKeys;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.game.models.Key.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Clase que gestiona todos los eventos y estados del teclado en el contexto GLFW.
 * <p>
 * Proporciona funciones callback para GLFW que permiten detectar las pulsaciones y liberaciones de teclas, manteniendo el estado
 * actual de todas las teclas del teclado.
 * <p>
 * La clase mantiene un registro de la ultima tecla presionada y la ultima tecla de movimiento presionada, lo que facilita la
 * gestion de las acciones de navegacion del jugador. Ofrece metodos para verificar si una tecla esta siendo presionada en tiempo
 * real o si se debe procesar una accion especifica una sola vez por cada pulsacion de tecla.
 * <p>
 * Incluye funcionalidad para gestionar combinaciones de teclas como {@code Ctrl}, {@code Shift}, {@code Alt} y {@code Super}, y
 * se integra con {@code ImGui} para sincronizar adecuadamente el estado de las teclas entre el motor grafico y la interfaz.
 */

public enum KeyListener {

    INSTANCE;

    private static final ImGuiIO IM_GUI_IO = ImGui.getIO();
    private static final BindKeys BIND_KEYS = BindKeys.INSTANCE;
    private static final boolean[] KEY_PRESSED = new boolean[350];
    public static final List<Integer> LAST_KEYS_MOVED_PRESSED = new ArrayList<>();
    private static int lastKeyMovedPressed;
    private static int lastKeyPressed;

    /**
     * @desc: Funcion callBack para gestionar el listener de teclas de nuestra ventana GLFW
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            KEY_PRESSED[key] = true;
            IM_GUI_IO.setKeysDown(key, true);

            lastKeyPressed = key;

            if (key == BIND_KEYS.getBindedKey(UP) || key == BIND_KEYS.getBindedKey(LEFT) ||
                    key == BIND_KEYS.getBindedKey(DOWN) || key == BIND_KEYS.getBindedKey(RIGHT)) {
                lastKeyMovedPressed = key;
                LAST_KEYS_MOVED_PRESSED.add(key);
            }

        } else if (action == GLFW_RELEASE) {
            KEY_PRESSED[key] = false;
            IM_GUI_IO.setKeysDown(key, false);
            if (key == BIND_KEYS.getBindedKey(UP) || key == BIND_KEYS.getBindedKey(LEFT) ||
                    key == BIND_KEYS.getBindedKey(DOWN) || key == BIND_KEYS.getBindedKey(RIGHT)) {
                LAST_KEYS_MOVED_PRESSED.remove(LAST_KEYS_MOVED_PRESSED.indexOf(key));
            }
        }

        IM_GUI_IO.setKeyCtrl(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_CONTROL) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
        IM_GUI_IO.setKeyShift(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_SHIFT) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
        IM_GUI_IO.setKeyAlt(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_ALT) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_ALT));
        IM_GUI_IO.setKeySuper(IM_GUI_IO.getKeysDown(GLFW_KEY_LEFT_SUPER) || IM_GUI_IO.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    }

    /**
     * @desc: Devuelve true si la tecla esta siendo presionada, caso contrario false.
     */
    public static boolean isKeyPressed(int keyCode) {
        return KEY_PRESSED[keyCode];
    }

    /**
     * @desc: Sirve para que cuando presionemos una tecla detecte si realizo su accion correspondiente, ya que si utilizamos la
     * funcion "isKeyPressed" va a seguir ejecutando la accion en el main loop del juego (como hacemos con la caminata).
     */
    public static boolean isKeyReadyForAction(int keyCode) {
        boolean retVal = KEY_PRESSED[keyCode];
        if (retVal) KEY_PRESSED[keyCode] = false;
        return retVal;
    }

    /**
     * @desc: Devulve la ultima tecla presionada por el usuario.
     */
    public static int getLastKeyPressed() {
        return lastKeyPressed;
    }

    public static void setLastKeyPressed(int value) {
        lastKeyPressed = value;
    }

    public static int getLastKeyMovedPressed() {
        return lastKeyMovedPressed;
    }

    public static void setLastKeyMovedPressed(int value) {
        lastKeyMovedPressed = value;
    }

}

