package org.aoclient.engine.renderer;

/**
 * Clase que representa un color en formato RGB utilizando valores de punto flotante.
 * <p>
 * Esta clase sirve para almacenar y manipular mezclas de colores utilizados en el sistema de renderizado. Los valores de cada
 * componente (rojo, verde y azul) son almacenados como numeros de punto flotante en un rango de 0.0 a 1.0, donde 0.0 representa
 * la ausencia del color y 1.0 la maxima intensidad.
 * <p>
 * Por defecto, si se crea una instancia sin parametros, se inicializa con los valores (1.0, 1.0, 1.0) que corresponde al color
 * blanco.
 */

public class RGBColor {

    private float red;
    private float green;
    private float blue;

    public RGBColor() {
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    public RGBColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

}
