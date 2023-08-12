package org.aoclient.engine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public final class Time {
    //timers de AO
    private static final int ENGINE_BASE_SPEED = 18; // int en float

    private static float timerFPS = 1.0f; // 1 seg
    private static int contFPS = 0;

    // my timers
    public static float beginTime;
    public static float endTime;
    public static float deltaTime;
    public static float timerTicksPerFrame;

    // Frames per second
    public static long FPS;

    public static void initTimers() {
        beginTime = (float) glfwGetTime();
        deltaTime = -1.0f;
    }

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


    /**
     * Actualizamos deltaTime
     */
    public static void updateTimers() {
        updateFPS();

        endTime = (float) glfwGetTime();
        deltaTime = endTime - beginTime;
        beginTime = endTime;

        timerTicksPerFrame = (deltaTime * ENGINE_BASE_SPEED);
    }
}
