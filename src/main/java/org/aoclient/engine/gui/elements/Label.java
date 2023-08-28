package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.FontText;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.renderer.FontText.getSizeText;

/**
 * Clase Label heredada de la clase abstracta ElementGUI
 *
 * Aca esta toda la logica de como crear labels, dibujarlo, etc.
 */
public class Label extends ElementGUI {
    protected String text;
    protected RGBColor color;
    protected boolean bold, italic, center;

    /**
     * Sobrecarga de constructores para que se pueda crear un Label de distintas formas.
     */
    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic) {
        super(x, y, width, height);
        this.text = text;
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = bold;
        this.italic = italic;
        this.center = false;
    }

    public Label(int x, int y, boolean bold, RGBColor color) {
        super(x, y, 0, 0);
        this.text = "";
        this.color = color;
        this.bold = bold;
        this.italic = false;
        this.center = false;
    }

    public Label(int x, int y, boolean bold, boolean center, RGBColor color) {
        super(x, y, 0, 0);
        this.text = "";
        this.color = color;
        this.bold = bold;
        this.italic = false;
        this.center = center;
    }

    public Label(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = "";
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = false;
        this.italic = false;
        this.center = false;
    }

    public Label(int x, int y, int width, int height, boolean center) {
        super(x, y, width, height);
        this.text = "";
        this.color = new RGBColor(1.0f, 1.0f, 1.0f);
        this.bold = false;
        this.italic = false;
        this.center = center;
    }

    public Label(int x, int y, int width, int height, RGBColor color) {
        super(x, y, width, height);
        this.text = "";
        this.color = color;
        this.bold = false;
        this.italic = false;
        this.center = false;
    }

    public Label(String text, int x, int y, boolean bold, boolean italic, RGBColor color) {
        super(x, y, 0, 0);
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.center = false;
    }

    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic, RGBColor color) {
        super(x, y, width, height);
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.center = false;
    }

    public Label(String text, int x, int y, int width, int height, boolean bold, boolean italic, boolean center, RGBColor color) {
        super(x, y, width, height);
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.center = true;
    }

    /**
     * @desc: Dibujamos el Label segun los atributos establecdos.
     */
    @Override
    public void render() {
        if (center) {
            FontText.drawText(this.text, (this.x + (this.width / 2)) - (getSizeText(this.text) / 2), this.y, this.color, 0, this.bold, this.italic, true);
        } else {
            FontText.drawText(this.text, this.x, this.y, this.color, 0, this.bold, this.italic, true);
        }
    }

    /**
     * @return Getter del atributo text.
     */
    public String getText() {
        return text;
    }

    /**
     * @desc Setter del atributo text.
     */
    public void setText(String text) {
        this.text = text;
    }
}
