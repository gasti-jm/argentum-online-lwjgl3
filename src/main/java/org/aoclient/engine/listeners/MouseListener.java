package org.aoclient.engine.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean mouseButtonDobleClickPressed[] = new boolean[3];
    private boolean isDragging;
    private static final double DOUBLE_CLICK_TIME = 0.3;
    private double lastTimeClick;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.lastTimeClick = 0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;

                // Doble click!
                double currentTime = glfwGetTime();

                if (currentTime - get().lastTimeClick  <= DOUBLE_CLICK_TIME) {
                    get().mouseButtonDobleClickPressed[button] = true;
                } else {
                    get().mouseButtonDobleClickPressed[button] = false;
                }

                get().lastTimeClick = currentTime;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }

        return false;
    }

    /**
     * Detecta si hicimos 1 click
     */
    public static boolean mouseButtonClick(int button) {
        if (button < get().mouseButtonPressed.length) {
            boolean retVal = get().mouseButtonPressed[button];

            if (retVal) {
                get().mouseButtonPressed[button] = false;
            }

            return retVal;
        } else {
            return false;
        }
    }

    /**
     * Detecta si hicimos doble click
     */
    public static boolean mouseButtonDoubleClick(int button) {
        if (button < get().mouseButtonPressed.length) {
            if(get().mouseButtonDobleClickPressed[button]){
                get().mouseButtonDobleClickPressed[button] = false;
                return true;
            }
        }

        return false;
    }
}

