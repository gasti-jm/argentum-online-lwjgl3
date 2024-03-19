package org.aoclient.engine.game;

import static org.aoclient.engine.utils.Time.deltaTime;

/**
 * Esta es la clase que gestiona y crea intervalos para el juego.
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

    private float currentTime;
    private final float interval;

    public IntervalTimer(final float interval) {
        this.currentTime = interval;
        this.interval = interval;
    }

    /**
     * @desc Actualiza el intervalo.
     */
    public void update() {
        if(this.currentTime < interval) {
            this.currentTime += deltaTime;
        }
    }

    /**
     * @desc Esta es la funcion en la que vamos a preguntar si nuestro intervalo ya paso y podemos accionar.
     */
    public boolean check(){
        if(this.currentTime >= interval) {
            this.currentTime = 0.0f;
            return true;
        }

        return false;
    }

}
