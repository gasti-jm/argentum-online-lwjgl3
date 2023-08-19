package org.aoclient.engine.gui.elements;

import org.aoclient.engine.renderer.RGBColor;

public class TextBox extends Label {
    private RGBColor backgroundColor;

    public TextBox(int x, int y, int width, int height) {
        super("", x, y, width, height);
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render() {

    }
}
