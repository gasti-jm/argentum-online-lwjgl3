package org.aoclient.engine.game;

import static org.aoclient.engine.utils.Time.deltaTime;

/**
 * <p>
 * Clase que gestiona los temporizadores e intervalos de tiempo para las diferentes acciones, controlando la frecuencia con la que
 * pueden ejecutarse.
 * <p>
 * Esta clase implementa un sistema de intervalos que regula cuando las acciones del jugador pueden ser ejecutadas nuevamente,
 * evitando su uso excesivo y manteniendo el equilibrio del juego. Proporciona constantes predefinidas para los tiempos de espera
 * de las distintas acciones como ataques, lanzamiento de hechizos, trabajo y uso de items.
 * <p>
 * Cada instancia de esta clase representa un temporizador independiente que puede ser actualizado con el ciclo de juego y
 * consultado para determinar si una accion especifica puede ser ejecutada en un momento dado.
 * <p>
 * Entre sus responsabilidades principales se encuentran:
 * <ul>
 * <li>Definir intervalos estandar para las diferentes acciones del juego
 * <li>Controlar el tiempo transcurrido desde la ultima accion realizada
 * <li>Determinar cuando una accion puede ser ejecutada nuevamente
 * <li>Proporcionar metodos para actualizar y verificar estados de los temporizadores
 * </ul>
 * <p>
 * La clase es fundamental para implementar las restricciones temporales de las mecanicas, asegurando que las acciones respeten
 * los tiempos de cooldown establecidos por el diseño del juego.
 */

public final class IntervalTimer {

    // Intervalos de AO.
    public static final float INT_MACRO_HECHIS = 2.788f;
    public static final float INT_MACRO_TRABAJO = 0.9f;
    public static final float INT_ATTACK = 1.5f;
    public static final float INT_ARROWS = 1.4f;
    public static final float INT_CAST_SPELL = 1.4f;
    public static final float INT_CAST_ATTACK = 1.0f;
    public static final float INT_WORK = 0.7f;
    public static final float INT_USEITEMDCK = 0.125f;
    public static final float INT_SENTRPU = 2.0f;
    private final float interval;
    private float currentTime;

    public IntervalTimer(final float interval) {
        this.currentTime = interval;
        this.interval = interval;
    }

    /**
     *  Actualiza el intervalo.
     */
    public void update() {
        if (this.currentTime < interval) this.currentTime += deltaTime;
    }

    /**
     *  Esta es la funcion en la que vamos a preguntar si nuestro intervalo ya paso y podemos accionar.
     */
    public boolean check() {
        if (this.currentTime >= interval) {
            this.currentTime = 0.0f;
            return true;
        }
        return false;
    }

}
