package org.aoclient.engine.renderer;

/**
 * Sirve para almacenar una mezcla de colores con alpha.
 */

public final class RGBAColor extends RGBColor {

    private float alpha;

    public RGBAColor() {

    }

    public RGBAColor(float alpha) {
        this.alpha = alpha;
    }

    public RGBAColor(float red, float green, float blue, float alpha) {
        super(red, green, blue);
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

}
