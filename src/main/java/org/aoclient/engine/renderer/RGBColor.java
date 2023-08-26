package org.aoclient.engine.renderer;

/**
 * Sirve para almacenar una mezcla de colores y se utiliza en el dibujado.
 */
public class RGBColor{
    private float red;
    private float green;
    private float blue;

    public RGBColor() {

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
