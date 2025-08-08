package org.aoclient.engine.renderer;

import static org.aoclient.engine.utils.Time.deltaTime;

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

    /**
     * Incrementa el valor de la intencidad del color rojo hasta toR a cierta velocidad.
     */
    public void incR(float speed, float toR) {
        this.red += speed * deltaTime;
        if (this.red >= toR) this.red = toR;
    }

    /**
     * Incrementa el valor de la intencidad del color verde hasta toG a cierta velocidad.
     */
    public void incG(float speed, float toG) {
        this.green += speed * deltaTime;
        if (this.green >= toG) this.green = toG;
    }

    /**
     * Incrementa el valor de la intencidad del color azul hasta toB a cierta velocidad.
     */
    public void incB(float speed, float toB) {
        this.blue += speed * deltaTime;
        if (this.blue >= toB) this.blue = toB;
    }


    /**
     * Reduce el valor de la intencidad del color rojo hasta toR a cierta velocidad.
     */
    public void decR(float speed, float toR) {
        this.red -= speed * deltaTime;
        if (this.red <= toR) this.red = toR;
    }

    /**
     * Reduce el valor de la intencidad del color verde hasta toG a cierta velocidad.
     */
    public void decG(float speed, float toG) {
        this.green -= speed * deltaTime;
        if (this.green <= toG) this.green = toG;
    }

    /**
     * Reduce el valor de la intencidad del color azul hasta toB a cierta velocidad.
     */
    public void decB(float speed, float toB) {
        this.blue -= speed * deltaTime;
        if (this.blue <= toB) this.blue = toB;
    }

}
