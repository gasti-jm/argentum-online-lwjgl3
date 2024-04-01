package org.aoclient.engine.scenes;

import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;
import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;


/**
 * Escena de presentacion
 *
 * Se recomienda leer el JavaDoc de la clase padre "Scene.java".
 */
public final class IntroScene extends Scene {
    private float   timeScene           = 15.0f; // 15 segundos de intro.
    private float   timeLogo            = 5.0f; // 5seg
    private float   timePresentation    = 3.33f;
    private int     nextInterface       = 1;
    private float   alphaInterface;

    private Texture[] imgs;

    @Override
    public void init() {
        super.init();
        this.alphaInterface = 0.0f;
        this.canChangeTo = SceneType.MAIN_SCENE;
        this.imgs = new Texture[4];

        this.imgs[0] = Surface.get().createTexture("resources/gui/noland.jpg", true);
        this.imgs[1] = Surface.get().createTexture("resources/gui/Presentacion5.jpg", true);
        this.imgs[2] = Surface.get().createTexture("resources/gui/Presentacion6.jpg", true);
        this.imgs[3] = Surface.get().createTexture("resources/gui/Presentacion7.jpg", true);
    }

    @Override
    public void mouseEvents() {
        // nothing to do..
    }

    /**
     * @desc: En caso de apretar enter: cerrame la presentacion y mostrame el conectar.
     */
    @Override
    public void keyEvents() {
        if (KeyListener.isKeyReadyForAction(GLFW_KEY_ENTER)) {
            close();
        }
    }

    /**
     * @desc: Renderiza nuestra escena y actualiza el efecto y el tiempo de presentancion.
     */
    @Override
    public void render() {
        if(!visible) return; // Si no dejamos esto, es posible que al cerrarse la escena genere un NullPointerException.

        // mientras no termine su tiempo se va a renderizar el efecto del logo.
        if(timeLogo >= 0) {
            effectNoLandStudios();
        } else {
            showPresentation();
        }

        checkEndScene();
    }

    /**
     * @desc: Muestra y cambia la interfaz de presentacion (ya que son varias imagenes)
     */
    private void showPresentation() {
        if (timePresentation <= 0 && nextInterface < 3) {
            timePresentation = 3.75f;
            nextInterface++;
        }

        geometryBoxRenderGUI(imgs[nextInterface], 0, 0, alphaInterface);

        timePresentation -= deltaTime;
    }

    /**
     * @desc: Actualiza el tiempo total de la escena y al finalizar cambia de escena.
     */
    private void checkEndScene() {
        timeScene -= deltaTime;

        if(timeScene <= 0) {
            close();
        }
    }

    /**
     * @desc: Prepara el cierre de la escena.
     */
    @Override
    public void close() {
        this.visible = false;
    }

    /**
     * @desc: Efecto de logo No-Land Studios
     */
    private void effectNoLandStudios() {
        alphaInterface += 0.3f * deltaTime;
        geometryBoxRenderGUI(imgs[0], 0, 0, alphaInterface);
        timeLogo -= deltaTime;
    }

}
