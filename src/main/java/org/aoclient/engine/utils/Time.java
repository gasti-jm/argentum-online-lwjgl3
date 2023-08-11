package org.aoclient.engine.utils;

public class Time {
    private static long end_time = 0;
    public static final float engineBaseSpeed = 0.018f;

    // Frames per second
    private static long frameTimer;
    private static long contFPS;
    public static long FPS;

    public static long timerElapsedTime;
    public static float timerTicksPerFrame;

    public static long getTime() {
        return System.nanoTime() / 1000000; //glfwGetTime();
    }

    public static long getElapsedTime() {
        long start_time = getTime();
        long ms = (start_time - end_time);
        end_time = getTime();
        return ms;
    }

    /**
     * Actualizamos deltaTime, FPS y la velocidad del engine.
     */
    public static void updateTimers() {
        // update fps and times
        if (getTime() >= frameTimer + 1000) {
            frameTimer = getTime();
            FPS = contFPS;
            contFPS = 0;
        }

        contFPS++;
        timerElapsedTime = getElapsedTime();
        timerTicksPerFrame = (timerElapsedTime * engineBaseSpeed);
    }
}
