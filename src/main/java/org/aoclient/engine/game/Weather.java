package org.aoclient.engine.game;

import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.game.Weather.TypeWeather.*;
import static org.aoclient.engine.utils.GameData.bLluvia;
import static org.aoclient.engine.utils.Time.deltaTime;

public enum Weather {
    INSTANCE;

    public enum TypeWeather {
        DAY(1f, 1f, 1f),
        MORNING(0.5f, 0.5f, 0.3f),
        NIGHT(0.2f, 0.2f, 0.2f),
        INVATION(1f, 0.2f, 0.2f);

        private final float r, g, b;
        TypeWeather(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private static float hourTimer = 3.0f; // 3600segs == 60min.
    private boolean colorEvent;
    private TypeWeather actual;
    private RGBColor renderColor;

    Weather() {
        this.actual = DAY; // por ahora, la idea es que lo setee el server y que el GM tenga comandos de tiempo.
        this.renderColor = new RGBColor(actual.r, actual.g, actual.b);
        this.colorEvent = false;
    }

    /**
     * Actualizamos los timers para el cambio de clima y/o efectos.
     *
     * TODO: Falta generar el efecto de cambio de color progresivo.
     */
    public void update() {
        // paso 1h?
        if (hourTimer <= 0) {
            hourTimer = 3.0f;
            this.changeWeather(null);
        }

        this.checkEffect();
        //System.err.println(actual + ": " + renderColor.getRed() + " " + renderColor.getGreen() + " " + renderColor.getBlue());

        hourTimer -= deltaTime;
    }

    /**
     * Cambiamos el tiempo. <br><br>
     * Si "type" es null se cambia automaticamente por la hora
     * Si type no es null se setea un color que querramos y desactiva el cambio de clima por hora.
     */
    private void changeWeather(TypeWeather type) {
        if (colorEvent) return; // no actualizamos por hora en caso de que estemos en evento.

        if(type == null) {
            switch (actual) {
                case NIGHT: actual = MORNING; break;
                case MORNING: actual = DAY; break;
                case DAY: actual = NIGHT; break;
            }
        } else {
            colorEvent = true;
            actual = type; // para Invasion o algun otro color falopa.
        }
    }

    /**
     * Genera el cambio progresivo de color al cambiar el tiempo (dia, noche, etc)...
     */
    private void checkEffect() {
        final float speed = 0.5f;

        // red
        if(renderColor.getRed() < actual.r) {
            renderColor.incR(speed, actual.r);
        } else {
            renderColor.decR(speed, actual.r);
        }

        // green
        if(renderColor.getGreen() < actual.g) {
            renderColor.incG(speed, actual.g);
        } else {
            renderColor.decG(speed, actual.g);
        }

        // blue
        if(renderColor.getBlue() < actual.b) {
            renderColor.incB(speed, actual.b);
        } else {
            renderColor.decB(speed, actual.b);
        }


    }

    /**
     * Devuelve el color del clima para ser dibujado en el render.\
     */
    public RGBColor getWeatherColor() {
        // No entiendo pq no esta seteado desde el mapInfo...
        // Basicamente en las dungeons siempre (o en este caso, los mapas que no llueve) tendran el color de DIA.
        if(!bLluvia[User.INSTANCE.getUserMap()]) {
            renderColor.setRed(1f);
            renderColor.setBlue(1f);
            renderColor.setGreen(1f);
        }

        return renderColor;
    }

}
