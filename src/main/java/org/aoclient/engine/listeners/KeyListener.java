package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;
import org.aoclient.engine.game.BindKeys;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.game.models.E_KeyType.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Aca es donde se gestiona la logica de la deteccion de las teclas del teclado en nuestro contexto GLFW.
 * 
 * Hay funciones callBack que se pasan en la configuracion de nuestra ventana.
 */
public class KeyListener {
    private static KeyListener instance;
    private static final ImGuiIO io = ImGui.getIO();


    private final BindKeys bindKeys = BindKeys.get();

    private final boolean[] keyPressed = new boolean[350];
    private int lastKeyMovedPressed;
    private int lastKeyPressed;
    public static List<Integer> lastKeysMovedPressed = new ArrayList<>();

    private KeyListener() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton).
     */
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    /**
     * @desc: Funcion callBack para gestionar el listener de teclas de nuestra ventana GLFW
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            io.setKeysDown(key, true);

            get().lastKeyPressed = key;

            if (key == get().bindKeys.getBindedKey(mKeyUp) || key == get().bindKeys.getBindedKey(mKeyLeft) ||
                    key == get().bindKeys.getBindedKey(mKeyDown) || key == get().bindKeys.getBindedKey(mKeyRight)) {

                get().lastKeyMovedPressed = key;
                lastKeysMovedPressed.add(key);
            }

        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            io.setKeysDown(key, false);

            if (key == get().bindKeys.getBindedKey(mKeyUp) || key == get().bindKeys.getBindedKey(mKeyLeft) ||
                    key == get().bindKeys.getBindedKey(mKeyDown) || key == get().bindKeys.getBindedKey(mKeyRight)) {

                 lastKeysMovedPressed.remove(lastKeysMovedPressed.indexOf(key));
            }
        }

        io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
        io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
        io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
        io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    }

    /**
     * @desc: Devuelve true si la tecla esta siendo presionada, caso contrario false.
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    /**
     * @desc: Sirve para que cuando presionemos una tecla detecte si realizo su accion
     *        correspondiente, ya que si utilizamos la funcion "isKeyPressed" va a seguir
     *        ejecutando la accion en el main loop del juego (como hacemos con la caminata).
     */
    public static boolean isKeyReadyForAction(int keyCode) {
        boolean retVal = get().keyPressed[keyCode];

        if (retVal) {
            get().keyPressed[keyCode] = false;
        }

        return retVal;
    }

    /**
     * @desc: Devulve la ultima tecla presionada por el usuario.
     */
    public static int getLastKeyPressed () {
        return get().lastKeyPressed;
    }

    public static void setLastKeyPressed(int value) {
        get().lastKeyPressed = value;
    }

    public static int getLastKeyMovedPressed () {
        return get().lastKeyMovedPressed;
    }

    public static void setLastKeyMovedPressed(int value) {
        get().lastKeyMovedPressed = value;
    }
}

