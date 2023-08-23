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

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.pressed = false;
    }

    /**
     * @desc: Dibujamos el boton, segun el estado del mouse y en caso de no tener textura ni se gasta en dibujarlo.
     */
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
                if (texture.get(BUTTON_NORMAL.ordinal()) != null) {
                    geometryBoxRenderGUI(this.texture.get(BUTTON_NORMAL.ordinal()), x, y, 1.0f);
                }
            }
        }
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    /**
     * @desc: Ejecuta la accion del boton (si fue apretado), y reproduce un sonido.
     */
    public void runAction() {
        if(action != null && isInside() && pressed) {
            playSound(SND_CLICK);
            this.action.run();
        }

        if(pressed) this.pressed = false;
    }

    /**
     * @desc: Borramos el boton reservado en memoria (su textura y runnable)
     */
    @Override
    public void clear() {
        super.clear();
        action = null;
    }

    public void setPressed(boolean value) {
        this.pressed = value;
    }

}
