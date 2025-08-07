package org.aoclient.engine.game;

import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.game.Weather.TypeWeather.*;
import static org.aoclient.engine.utils.GameData.bLluvia;
import static org.aoclient.engine.utils.Time.deltaTime;

public enum Weather {
    INSTANCE;

    public enum TypeWeather {
        MORNING(new RGBColor(0.7f, 0.7f, 0.5f)),
        DAY(new RGBColor(1.0f, 1.0f, 1.0f)),
        NIGHT(new RGBColor(0.2f, 0.2f, 0.2f)),
        INVATION(new RGBColor(1f, 0.2f, 0.2f));

        private final RGBColor color;

        TypeWeather(RGBColor color) {
            this.color = color;
        }

        public RGBColor getColor() {
            return color;
        }
    }

    private static float hourTimer = 3600.0f; // 3600segs == 60min.
    private boolean colorEvent;
    private TypeWeather actual;

    Weather() {
        this.actual = DAY; // por ahora, la idea es que lo setee el server y que el GM tenga comandos de tiempo.
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
            hourTimer = 3600.0f;
            this.changeWeather(null);
        }

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
                case MORNING -> actual = DAY;
                case DAY -> actual = NIGHT;
                case NIGHT -> actual = MORNING;
            }
        } else {
            colorEvent = true;
            actual = type; // para Invasion o algun otro color falopa.
        }
    }

    /**
     * Devuelve el color del clima para ser dibujado en el render.\
     */
    public RGBColor getWeatherColor() {
        // No entiendo pq no esta seteado desde el mapInfo...
        // Basicamente en las dungeons siempre (o en este caso, los mapas que no llueve) tendran el color de DIA.
        if(!bLluvia[User.INSTANCE.getUserMap()]) {
            return DAY.color;
        }

        return this.actual.getColor();
    }

}
