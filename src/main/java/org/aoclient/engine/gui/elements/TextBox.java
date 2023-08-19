package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.listeners.MouseListener;

import java.util.ArrayList;

public class TextBox extends ElementGUI {

    public TextBox(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {

    }

    public boolean isInside(){
        return MouseListener.getX() >= this.x && MouseListener.getX() <= this.x + this.width &&
                MouseListener.getY() >= this.y && MouseListener.getY() <= this.y + this.height;
    }
}
