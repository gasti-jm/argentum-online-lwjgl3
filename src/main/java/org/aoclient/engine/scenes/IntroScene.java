package org.aoclient.engine.scenes;

import org.aoclient.engine.listeners.KeyListener;

import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;

public class IntroScene extends Scene {
    private static float timeScene = 15.0f; // 10 segundos de intro.
    private float alphaBackground;

    @Override
    public void init() {
        super.init();
        this.alphaBackground = 0.0f;
        this.canChangeTo = SceneNames.GAME_SCENE;
    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(GLFW_KEY_ENTER) || KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            this.visible = false;
        }
    }

    @Override
    public void render() {
        effectWhiteScreen();
        timeScene -= deltaTime;

        if(timeScene <= 0) {
            this.visible = false;
        }
    }

    /**
     * @desc: Efecto de pantalla blanca.
     */
    private void effectWhiteScreen() {
        if(alphaBackground < 1.0f) {
            alphaBackground += 0.5f * deltaTime;

            background.setRed   (alphaBackground);
            background.setGreen (alphaBackground);
            background.setBlue  (alphaBackground);
        }
    }



}
