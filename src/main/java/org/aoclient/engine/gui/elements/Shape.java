package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.renderer.Drawn.drawRectangle;

/**
 * Clase Shape heredada de la clase abstracta ElementGUI
 *
 * Aca esta toda la logica de como crear Shape, dibujarlo, etc.
 */
public class Shape extends ElementGUI {
    private RGBColor color;

    public Shape (int x, int y, int w, int h, RGBColor color) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = color;
    }

    /**
     * @desc Dibuja nuestro shape como un rectangulo y color.
     */
    @Override
    public void render() {
        drawRectangle(x, y, width, height, color);
    }
}
