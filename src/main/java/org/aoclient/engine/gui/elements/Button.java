package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.gui.elements.ButtonStatus.*;
import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

public final class Button extends ElementGUI {
    private Runnable action;
    private boolean pressed;

    public Button() {
        this.pressed = false;
    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.pressed = false;
    }

    @Override
    public void render() {
        if(!texture.isEmpty()) {
            if(isInside()) {
                if(pressed) {
                    geometryBoxRenderGUI(this.texture.get(BUTTON_PRESSED.ordinal()), x, y, 1.0f);
                } else {
                    geometryBoxRenderGUI(this.texture.get(BUTTON_ROLLOVER.ordinal()), x, y, 1.0f);
                }

            } else {
                geometryBoxRenderGUI(this.texture.get(BUTTON_NORMAL.ordinal()), x, y, 1.0f);
            }
        }
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void runAction() {
        if(action != null && isInside() && pressed) {
            playSound(SND_CLICK);
            this.action.run();
        }

        if(pressed) this.pressed = false;
    }

    @Override
    public void clear() {
        super.clear();
        action = null;
    }

    public void setPressed(boolean value) {
        this.pressed = value;
    }

}
