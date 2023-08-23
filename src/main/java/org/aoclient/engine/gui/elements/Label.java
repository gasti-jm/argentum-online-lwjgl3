package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.FontText;
import org.aoclient.engine.renderer.RGBColor;

public class Label extends ElementGUI {
    protected String text;
    protected RGBColor color;
    protected boolean bold, italic;

    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic) {
        super(x, y, width, height);
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = bold;
        this.italic = italic;
    }

    public Label(int x, int y, boolean bold, RGBColor color) {
        super(x, y, 0, 0);
        this.text = "0/0";
        this.color = color;
        this.bold = bold;
        this.italic = false;
    }

    public Label(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = "";
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = false;
        this.italic = false;
    }

    public Label(int x, int y, int width, int height, RGBColor color) {
        super(x, y, width, height);
        this.text = "";
        this.color = color;
        this.bold = false;
        this.italic = false;
    }

    public Label(String text, int x, int y, boolean bold, boolean italic, RGBColor color) {
        super(x, y, 0, 0);
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic, RGBColor color) {
        super(x, y, width, height);
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    @Override
    public void render() {
        FontText.drawText(this.text, this.x, this.y, this.color, 0, this.bold, this.italic, true);
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
