package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.listeners.MouseListener;
import java.util.ArrayList;

import static org.aoclient.engine.gui.elements.ButtonStatus.*;
import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

public final class Button extends ElementGUI {
    private Runnable action;

    public Button() {

    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {
//        if(!isInside() && !MouseListener.isDragging()) {
//            geometryBoxRenderGUI(this.texture.get(BUTTON_NORMAL.ordinal()), x, y,
//                    this.texture.get(BUTTON_NORMAL.ordinal()).getTex_width(), this.texture.get(BUTTON_NORMAL.ordinal()).getTex_height(), 0, 0, false, 1.0f);
//        } else if (isInside()) {
//            geometryBoxRenderGUI(this.texture.get(BUTTON_HOVER.ordinal()), x, y,
//                    this.texture.get(BUTTON_HOVER.ordinal()).getTex_width(), this.texture.get(BUTTON_HOVER.ordinal()).getTex_height(), 0, 0, false, 1.0f);
//        } else {
//            geometryBoxRenderGUI(this.texture.get(BUTTON_PRESSED.ordinal()), x, y,
//                    this.texture.get(BUTTON_PRESSED.ordinal()).getTex_width(), this.texture.get(BUTTON_PRESSED.ordinal()).getTex_height(), 0, 0, false, 1.0f);
//        }
    }

    private boolean isInside(){
        return MouseListener.getX() >= this.x && MouseListener.getX() <= this.x + this.width &&
                MouseListener.getY() >= this.y && MouseListener.getY() <= this.y + this.height;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void runAction() {
        if(isInside()) {
            this.action.run();
        } else {
            System.out.println("Estas haciendo click fuera del boton.");
        }
    }



}
