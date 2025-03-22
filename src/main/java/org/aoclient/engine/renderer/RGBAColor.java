package org.aoclient.engine.renderer;

/**
 * Clase que extiende {@code RGBColor} para representar colores con transparencia en formato RGBA.
 * <p>
 * Mientras que RGBColor maneja los tres componentes basicos de color (rojo, verde y azul), RGBAColor añade un cuarto componente:
 * el canal alfa, que controla la transparencia del color. Este componente alfa tambien se representa como un valor de punto
 * flotante en el rango de 0.0 a 1.0, donde 0.0 significa completamente transparente y 1.0 completamente opaco.
 * <p>
 * Esta clase es especialmente util para efectos visuales que requieren transparencia, como desvanecimientos, superposiciones
 * parciales y efectos de transicion. Proporciona constructores y metodos adicionales para gestionar el componente alfa mientras
 * hereda toda la funcionalidad para manejar los componentes RGB.
 * <p>
 * RGBAColor se utiliza en el sistema de renderizado cuando se necesita controlar no solo el color de un elemento sino tambien su
 * nivel de transparencia.
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
