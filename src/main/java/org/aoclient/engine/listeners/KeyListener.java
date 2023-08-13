package org.aoclient.engine.listeners;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private int lastKeyPressed;
    private static boolean actionKey[] = new boolean[350];
    public static List<Integer> lastKeysPressed = new ArrayList<>();

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
            actionKey[key] = true; // estoy listo para accionar cualquier cosa.

            if (key == GLFW_KEY_W || key == GLFW_KEY_A || key == GLFW_KEY_S || key == GLFW_KEY_D) {
                get().lastKeyPressed = key;
                lastKeysPressed.add(key);
            }

        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;

            if (key == GLFW_KEY_W || key == GLFW_KEY_A || key == GLFW_KEY_S || key == GLFW_KEY_D) {
                lastKeysPressed.remove(lastKeysPressed.indexOf(key));
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
        boolean retVal = actionKey[keyCode];

        if (retVal) {
            actionKey[keyCode] = false;
        }

        return retVal;
    }

    /**
     * @desc: Devulve la ultima tecla presionada por el usuario.
     */
    public static int getLastKeyPressed () {
        return get().lastKeyPressed;
    }
}

