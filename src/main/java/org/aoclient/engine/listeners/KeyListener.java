package org.aoclient.engine.listeners;

import org.aoclient.engine.game.BindKeys;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.game.E_KeyType.*;
import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;

    private final BindKeys bindKeys = BindKeys.get();
    private final boolean[] keyPressed = new boolean[350];
    private int lastKeyMovedPressed;
    private int lastKeyPressed;
    public static List<Integer> lastKeysMovedPressed = new ArrayList<>();

    private KeyListener() {

    }

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
            get().lastKeyPressed = key;

            if (key == get().bindKeys.getBindedKey(mKeyUp) || key == get().bindKeys.getBindedKey(mKeyLeft) ||
                    key == get().bindKeys.getBindedKey(mKeyDown) || key == get().bindKeys.getBindedKey(mKeyRight)) {

                get().lastKeyMovedPressed = key;
                lastKeysMovedPressed.add(key);
            }

        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;

            if (key == get().bindKeys.getBindedKey(mKeyUp) || key == get().bindKeys.getBindedKey(mKeyLeft) ||
                    key == get().bindKeys.getBindedKey(mKeyDown) || key == get().bindKeys.getBindedKey(mKeyRight)) {

                 lastKeysMovedPressed.remove(lastKeysMovedPressed.indexOf(key));
            }
        }
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

