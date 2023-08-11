package org.aoclient.engine;

import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.scenes.MainScene;
import org.aoclient.engine.scenes.Scene;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.Time;
import org.lwjgl.Version;

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

        currentScene = new MainScene();
        currentScene.init();

        loop();
        close();
    }

    private void close() {
        window.close();
    }

    private void loop() {
        while (prgRun) {
            glfwPollEvents();

            if (!window.isMinimized()) {
                glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                render();

                glfwSwapBuffers(window.getWindow());
            }
        }
    }

    private void render() {
        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            this.prgRun = false;
        }

        currentScene.keyEvents();
        currentScene.render();
        Time.updateTimers();
    }


}
