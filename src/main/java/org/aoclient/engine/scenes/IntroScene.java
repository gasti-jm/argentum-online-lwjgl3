package org.aoclient.engine.scenes;

import org.aoclient.engine.listeners.KeyListener;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;


/**
 * Escena de presentacion (frmCargando - frmPresentacion).
 *
 * Se recomienda leer el JavaDoc de la clase padre "Scene.java".
 */
public final class IntroScene extends Scene {
    private static float timeScene = 15.0f; // 15 segundos de intro.
    private static float timeLogo = 5.0f; // 5seg
    private static float timePresentation = 3.33f;

    private float alphaInterface;
    private int nextInterface = 1;


    @Override
    public void init() {
        super.init();
        this.alphaInterface = 0.0f;
        this.canChangeTo = SceneType.MAIN_SCENE;

        // agregamos 4 interfaces
//        for(int i = 0; i < 4; i++){
//            this.images.add(new ImageBox());
//        }

        // le cargamos las texturas
//        this.images.get(0).loadTextures("noland.jpg"); // efecto
//        this.images.get(1).loadTextures("Presentacion5.jpg");
//        this.images.get(2).loadTextures("Presentacion6.jpg");
//        this.images.get(3).loadTextures("Presentacion7.jpg");
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

//        images.get(nextInterface).render();

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
//        images.get(0).setAlphaTexture(alphaInterface);
//        images.get(0).render();

        timeLogo -= deltaTime;
    }



}
