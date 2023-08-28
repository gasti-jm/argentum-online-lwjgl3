package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.gui.elements.ButtonStatus.*;
import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

/**
 * Clase Button heredada de la clase abstracta ElementGUI
 *
 * Aca esta toda la logica de como crear botones, dibujarlo, etc.
 */
public final class Button extends ElementGUI {
    private Runnable action;
    private boolean pressed;

    /**
     * Sobrecarga de constructores para que se pueda crear un boton de distintas formas.
     */
    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.pressed = false;
    }

    public Button(int x, int y, int width, int height, Runnable action, String... textures) {
        super(x, y, width, height);
        this.pressed = false;
        this.action = action;
        this.loadTextures(textures);
    }

    public Button(int x, int y, int width, int height, Runnable action) {
        super(x, y, width, height);
        this.pressed = false;
        this.action = action;
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

    /**
     * @param action Funcion lambda
     * @desc Setter del atributo action.
     */
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

    /**
     * @param value Nuevo valor booleano
     * @desc Setter del atributo pressed.
     */
    public void setPressed(boolean value) {
        this.pressed = value;
    }

}
