package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.FontTypes;
import org.aoclient.engine.renderer.RGBColor;

public class Label extends ElementGUI {
    protected String text;
    protected RGBColor color;
    protected boolean bold, italic;

    public Label() {

    }

    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic) {
        super(x, y, width, height);
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = bold;
        this.italic = italic;
    }

    public Label(String text) {
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = false;
        this.italic = false;
    }

    @Override
    public void render() {
        FontTypes.drawText(this.text, this.x, this.y, this.color, 0, this.bold, this.italic, true);
    }
}
