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

    // Intervalos
    public static final float INT_MACRO_HECHIS = 2.788f;
    public static final float INT_MACRO_TRABAJO = 0.9f;
    public static final float INT_ATTACK = 1.5f;
    public static final float INT_ARROWS = 1.4f;
    public static final float INT_CAST_SPELL = 1.4f;
    public static final float INT_CAST_ATTACK = 1.0f;
    public static final float INT_WORK = 0.7f;
    public static final float INT_USEITEMU = 0.45f;
    public static final float INT_USEITEMDCK = 0.125f;
    public static final float INT_SENTRPU = 2.0f;

    /**
     * @desc: Inicializamos las variables necesarias para calcular el tiempo
     */
    public static void initTimers() {
        beginTime = (float) glfwGetTime();
        deltaTime = -1.0f;
    }

    /**
     * @desc: Actualizamos los timers, incluido los FPS
     */
    public static void updateTimers() {
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
