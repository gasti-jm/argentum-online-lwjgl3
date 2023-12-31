package org.aoclient.engine.gui.elements;

import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.FontText;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.Engine.capsState;
import static org.aoclient.engine.renderer.Drawn.drawRectangle;
import static org.aoclient.engine.renderer.FontText.drawText;
import static org.aoclient.engine.renderer.FontText.getSizeText;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Clase TextBox heredada de la clase Label, ya que contienen las mismas propiedades
 *
 * Aca esta toda la logica de como crear cajas de texto, dibujarlo, detectar eventos, identificar su tabindex, etc.
 */
public class TextBox extends Label {
    private final RGBColor backgroundColor;
    private final boolean hideChars;
    private final int tabIndex;
    private boolean selected = false;

    /**
     * Sobrecarga de constructores para que se pueda crear un TextBox de distintas formas.
     */
    public TextBox(int tabIndex, int x, int y, int width, int height, boolean bold, boolean italic, boolean hideChars, RGBColor color) {
        super("", x, y, width, height, bold, italic, color);
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = hideChars;
        this.center = false;
    }

    public TextBox(int tabIndex, int x, int y, int width, int height, boolean bold, boolean italic, boolean hideChars) {
        super("", x, y, width, height, bold, italic, new RGBColor(1.0f, 1.0f, 1.0f));
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = hideChars;
        this.center = false;
    }

    public TextBox(int tabIndex, int x, int y, int width, int height, boolean bold, boolean italic) {
        super("", x, y, width, height, bold, italic, new RGBColor(1.0f, 1.0f, 1.0f));
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = false;
        this.center = false;
    }

    public TextBox(int tabIndex, String text, int x, int y, int width, int height, boolean bold, boolean italic) {
        super(text, x, y, width, height, bold, italic, new RGBColor(1.0f, 1.0f, 1.0f));
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = false;
        this.center = false;
    }

    public TextBox(int tabIndex, String text, int x, int y, int width, int height, boolean bold, boolean italic, RGBColor color) {
        super(text, x, y, width, height, bold, italic, color);
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = false;
        this.center = false;
    }

    public TextBox(int tabIndex, String text, int x, int y, int width, int height, boolean bold, boolean italic, boolean center, RGBColor color) {
        super(text, x, y, width, height, bold, italic, color);
        this.tabIndex = tabIndex;
        this.backgroundColor = new RGBColor(0.0f, 0.0f, 0.0f);
        this.hideChars = false;
        this.center = true;
    }

    @Override
    public void render() {
        drawRectangle(x, y, width, height, backgroundColor);

        if (!hideChars) {
            if (center) {
                FontText.drawText(this.text, (this.x + (this.width / 2)) - (getSizeText(this.text) / 2), this.y, this.color, 0, this.bold, this.italic, false);
            } else {
                FontText.drawText(this.text, this.x, this.y, this.color, 0, this.bold, this.italic, false);
            }

        } else {
            StringBuilder txtHided = new StringBuilder();
            for (int i = 0; i < this.text.length(); i++) txtHided.append("*");
            drawText(txtHided.toString(), this.x, this.y, this.color, 0, this.bold, this.italic, false);
        }

    }

    /**
     *
     * @return -1 si no se esta selecionado el objeto TextBox (haciendo click), caso contrario devolvemos su tabIndex.
     */
    public int checkSelected() {
        if (isInside()) {
            if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
                return tabIndex;
            }
        }

        return -1;
    }

    /**
     * @desc Si tenemos el TextBox seleccionado, escuchamos las teclas para escribir o borrar texto.
     */
    public void keyEvents() {
        if (!selected) return;
        final int keyCode = KeyListener.getLastKeyPressed();

        if (keyCode == GLFW_KEY_BACKSPACE) {
            if (KeyListener.isKeyReadyForAction(keyCode) && this.text.length() > 0) {
                this.text = this.text.substring(0, text.length() - 1);
            }
        } else {
            if (KeyListener.isKeyReadyForAction(keyCode)) { // estoy aprentando una tecla?
                if (keyCode >= GLFW_KEY_SPACE && keyCode <= GLFW_KEY_WORLD_2) { // caracteres del teclado.
                    if (capsState) {
                        this.text += Character.toString((char) keyCode);
                    } else {
                        this.text += Character.toString((char) keyCode).toLowerCase();
                    }
                }
            }
        }

    }

    /**
     * @desc Setter del atributo selected
     */
    public void setSelected(boolean value) {
        this.selected = value;
    }
}
