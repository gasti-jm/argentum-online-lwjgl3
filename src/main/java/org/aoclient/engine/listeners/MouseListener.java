package org.aoclient.engine.listeners;

import imgui.ImGui;
import imgui.ImGuiIO;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Clase que gestiona todos los eventos y estados del mouse en el contexto GLFW.
 * <p>
 * Proporciona funciones callback para GLFW que permiten detectar movimientos del cursor, pulsaciones de botones y desplazamiento
 * de la rueda del mouse.
 * <p>
 * Esta clase mantiene un registro constante de la posicion actual del mouse, su posicion anterior, el estado de los botones y
 * detecta acciones como: arrastrar elementos, clic simple, doble clic y desplazamiento. Proporciona funcionalidad para determinar
 * si el mouse esta sobre areas especificas y gestiona temporizadores para deteccion de doble clic.
 * <p>
 * Todos los demas componentes del juego que necesitan informacion sobre el estado del mouse o responder a eventos del mismo
 * utilizan esta clase a traves de sus metodos estaticos.
 * <p>
 * Tambien se encarga de sincronizar adecuadamente con {@code ImGui} para mantener consistencia en los eventos del mouse entre el
 * motor grafico y la interfaz.
 */

public enum MouseListener {

    INSTANCE;

    private static final double DOUBLE_CLICK_TIME = 0.5;
    private static final ImGuiIO IM_GUI_IO = ImGui.getIO();
    private static final boolean[] MOUSE_BUTTON_PRESSED = new boolean[3];
    private static final boolean[] MOUSE_BUTTON_DOBLE_CLICK_PRESSED = new boolean[3];
    private static final boolean[] MOUSE_BUTTON_RELEASED = new boolean[3];

    private static double scrollX, scrollY;
    private static double xPos, yPos;
    private static double lastY, lastX;
    private static boolean isDragging;
    private static final double[] lastTimeClick = new double[3];

    /**
     *  Funcion callBack para detectar y actualizar la posicion del mouse.
     */
    public static void mousePosCallback(long window, double xpos, double ypos) {
        lastX = xPos;
        lastY = yPos;
        xPos = xpos;
        yPos = ypos;
        isDragging = MOUSE_BUTTON_PRESSED[0] || MOUSE_BUTTON_PRESSED[1] || MOUSE_BUTTON_PRESSED[2];
    }

    /**
     *  Funcion callBack para detectar los botones pulsados.
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        final boolean[] mouseDown = new boolean[5];

        mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
        mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
        mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
        mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
        mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

        IM_GUI_IO.setMouseDown(mouseDown);

        if (!IM_GUI_IO.getWantCaptureMouse() && mouseDown[1]) ImGui.setWindowFocus(null);

        if (action == GLFW_PRESS) {
            if (button < MOUSE_BUTTON_PRESSED.length) {
                MOUSE_BUTTON_PRESSED[button] = true;
                isDragging = true;

                // Doble click!
                double currentTime = glfwGetTime();

                MOUSE_BUTTON_DOBLE_CLICK_PRESSED[button] = currentTime - lastTimeClick[button] <= DOUBLE_CLICK_TIME;

                lastTimeClick[button] = currentTime;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < MOUSE_BUTTON_PRESSED.length) {
                MOUSE_BUTTON_PRESSED[button] = false;
                MOUSE_BUTTON_RELEASED[button] = true;
                isDragging = false;
            }
        }
    }

    /**
     *  Funcion callBack para detectar el desplazamiento del mouse de una posicion a otra.
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        IM_GUI_IO.setMouseWheelH(IM_GUI_IO.getMouseWheelH() + (float) xOffset);
        IM_GUI_IO.setMouseWheel(IM_GUI_IO.getMouseWheel() + (float) yOffset);
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public static void endFrame() {
        scrollX = 0;
        scrollY = 0;
        lastX = xPos;
        lastY = yPos;
    }

    public static float getX() {
        return (float) xPos;
    }

    public static float getY() {
        return (float) yPos;
    }

    public static float getDx() {
        return (float) (lastX - xPos);
    }

    public static float getDy() {
        return (float) (lastY - yPos);
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }

    public static boolean isDragging() {
        return isDragging;
    }

    public static boolean pressing(int button) {
        return MOUSE_BUTTON_PRESSED[button];
    }

    /**
     * @param button Boton que querramos detectar
     * @return True si estamos apretamos un dicho boton, caso contrario false.
     */
    public static boolean mouseButtonDown(int button) {
        if (button < MOUSE_BUTTON_PRESSED.length) return MOUSE_BUTTON_PRESSED[button];
        return false;
    }

    /**
     * Detecta si hicimos 1 click
     */
    public static boolean mouseButtonClick(int button) {
        if (button < MOUSE_BUTTON_PRESSED.length) {
            boolean retVal = MOUSE_BUTTON_PRESSED[button];
            if (retVal) MOUSE_BUTTON_PRESSED[button] = false;
            return retVal;
        }
        return false;
    }

    /**
     * Detecta si hicimos doble click
     */
    public static boolean mouseButtonDoubleClick(int button) {
        if (button < MOUSE_BUTTON_PRESSED.length) {
            boolean retVal = MOUSE_BUTTON_DOBLE_CLICK_PRESSED[button];
            if (retVal) {
                lastTimeClick[button] = 0;
                MOUSE_BUTTON_DOBLE_CLICK_PRESSED[button] = false;
            }
            return retVal;
        }
        return false;
    }

    /**
     * Detecta si se soltó un botón del mouse en este frame.
     */
    public static boolean mouseButtonReleased(int button) {
        if (button < MOUSE_BUTTON_RELEASED.length) {
            boolean retVal = MOUSE_BUTTON_RELEASED[button];
            MOUSE_BUTTON_RELEASED[button] = false; // consumir evento
            return retVal;
        }
        return false;
    }

    public static void resetReleasedButtons() {
        for (int i = 0; i < MOUSE_BUTTON_RELEASED.length; i++) {
            MOUSE_BUTTON_RELEASED[i] = false;
        }
    }
}

