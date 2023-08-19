package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
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
        if(!texture.isEmpty()) {
            if (!isInside() && !MouseListener.isDragging()) {
                geometryBoxRenderGUI(this.texture.get(BUTTON_NORMAL.ordinal()), x, y,
                        this.texture.get(BUTTON_NORMAL.ordinal()).getTex_width(), this.texture.get(BUTTON_NORMAL.ordinal()).getTex_height(), 0, 0, false, 1.0f);
            } else if (isInside()) {
                geometryBoxRenderGUI(this.texture.get(BUTTON_ROLLOVER.ordinal()), x, y,
                        this.texture.get(BUTTON_ROLLOVER.ordinal()).getTex_width(), this.texture.get(BUTTON_ROLLOVER.ordinal()).getTex_height(), 0, 0, false, 1.0f);
            } else {
                geometryBoxRenderGUI(this.texture.get(BUTTON_PRESSED.ordinal()), x, y,
                        this.texture.get(BUTTON_PRESSED.ordinal()).getTex_width(), this.texture.get(BUTTON_PRESSED.ordinal()).getTex_height(), 0, 0, false, 1.0f);
            }
        }
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void runAction() {
        if(action != null && isInside()) {
            playSound(SND_CLICK);
            this.action.run();
        }
    }

    @Override
    public void clear() {
        super.clear();
        action = null;
    }

}
