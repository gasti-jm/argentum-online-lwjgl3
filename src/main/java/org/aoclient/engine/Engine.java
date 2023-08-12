package org.aoclient.engine;

import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.scenes.*;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.Time;
import org.lwjgl.Version;

import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {
    private boolean prgRun = true;
    private Window window;
    private Surface surface;
    private Scene currentScene;


    public void start() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        window = Window.getInstance();
        window.initializate();

        surface = Surface.getInstance();

        surface.initialize();
        GameData.initialize();

        changeScene(SceneNames.INTRO_SCENE);

        glEnable(GL_TEXTURE_2D);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        glViewport(0, 0, window.getWidth(), window.getHeight());
        glOrtho(0, window.getWidth(), window.getHeight(), 0, 1, -1);

        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glEnable(GL_ALPHA);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        loop();
        close();
    }

    private void close() {
        window.close();
    }

    private void loop() {
        Time.initTimers();

        while (prgRun) {
            glfwPollEvents();

            if (!window.isMinimized()) {
                glClearColor(currentScene.getBackground().getRed(), currentScene.getBackground().getGreen(), currentScene.getBackground().getBlue(), 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                if(deltaTime >= 0) {
                    render();
                }

                glfwSwapBuffers(window.getWindow());

                Time.updateTimers();
            }
        }
    }

    private void changeScene(SceneNames scene) {
        switch (scene) {
            case INTRO_SCENE:
                currentScene = new IntroScene();
                currentScene.init();
                break;

            case GAME_SCENE:
                currentScene = new GameScene();
                currentScene.init();
                break;

            case MAIN_SCENE:
                currentScene = new MainScene();
                currentScene.init();
                break;

        }
    }

    private void render() {
        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            this.prgRun = false;
        }

        checkChangeScene();
        currentScene.keyEvents();
        currentScene.render();
    }

    private void checkChangeScene() {
        if (!currentScene.isVisible()) {
            changeScene(currentScene.getChangeScene());
        }
    }

}
