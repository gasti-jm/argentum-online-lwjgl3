package org.aoclient.engine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * <p>
 * Es una clase final con metodos estaticos que proporciona funcionalidad esencial para el manejo del tiempo en el ciclo de
 * renderizado. Se encarga de calcular y rastrear el tiempo transcurrido entre fotogramas (deltaTime), lo que permite que las
 * animaciones y movimientos sean consistentes independientemente de la velocidad de renderizado.
 * <p>
 * Adicionalmente, esta clase contiene la logica para calcular y mostrar los FPS, lo que es util para medir el rendimiento del
 * juego.
 * <p>
 * Esta clase es crucial para que todos los elementos animados del juego (personajes, efectos, etc.) se muevan a velocidades
 * consistentes en diferentes sistemas, ajustandose automaticamente a las variaciones en la tasa de fotogramas.
 * <p>
 * Al ser una clase utilitaria con metodos estaticos, no es necesario instanciarla para usar su funcionalidad.
 *
 * @see GameData
 */

public final class Time {

    // Timers del juego
    private static final int ENGINE_BASE_SPEED = 18;
    public static float timerTicksPerFrame;
    public static int FPS;
    public static float beginTime;
    public static float endTime;
    public static float deltaTime;
    private static float timerFPS = 1.0f; // 1 seg
    private static int contFPS = 0;

    /**
     *  Inicializamos las variables necesarias para calcular el tiempo
     */
    public static void initTime() {
        beginTime = (float) glfwGetTime();
        deltaTime = -1.0f;
    }

    /**
     *  Actualizamos los timers, incluido los FPS
     */
    public static void updateTime() {
        updateFPS();

        endTime = (float) glfwGetTime();
        deltaTime = endTime - beginTime;
        beginTime = endTime;

        timerTicksPerFrame = (deltaTime * ENGINE_BASE_SPEED);
    }

    /**
     *  Actualizamos los FPS
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
