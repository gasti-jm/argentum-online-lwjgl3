package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.renderer.Drawn.drawText;

public class Label extends ElementGUI {
    protected String text;
    protected RGBColor color;

    public Label() {

    }

    public Label(String text, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
    }

    public Label(String text) {
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void render() {
        drawText(this.text, this.x, this.y, this.color, 0, false);
    }
}
