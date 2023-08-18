package org.aoclient.engine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public final class Time {
    // Timers del juego
    private static final int ENGINE_BASE_SPEED = 18;
    public static float timerTicksPerFrame;

    private static float timerFPS = 1.0f; // 1 seg
    private static int contFPS = 0;
    public static int FPS;

    public static float beginTime;
    public static float endTime;
    public static float deltaTime;

    /**
     * @desc: Inicializamos las variables necesarias para calcular el tiempo
     */
    public static void initTime() {
        beginTime = (float) glfwGetTime();
        deltaTime = -1.0f;
    }

    /**
     * @desc: Actualizamos los timers, incluido los FPS
     */
    public static void updateTime() {
        updateFPS();

        endTime = (float) glfwGetTime();
        deltaTime = endTime - beginTime;
        beginTime = endTime;

        timerTicksPerFrame = (deltaTime * ENGINE_BASE_SPEED);
    }

    /**
     * @desc: Actualizamos los FPS
     */
    private static void updateFPS() {
        // paso 1 seg?
        if (timerFPS <= 0) {
            timerFPS = 1.0f;
            FPS = contFPS;
            contFPS = 0;
        }

        contFPS++;
        timerFPS -= deltaTime;
    }
}
