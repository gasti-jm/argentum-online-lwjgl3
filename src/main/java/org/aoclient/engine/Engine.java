package org.aoclient.engine;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.game.models.E_KeyType;
import org.aoclient.engine.gui.forms.Form;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.scenes.*;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.Time;
import org.lwjgl.Version;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.scenes.SceneType.INTRO_SCENE;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    private static boolean prgRun = true;
    private Window window;
    private Scene currentScene;
    private BindKeys bindKeys;
    public static List<Form> forms = new ArrayList<>(); // formularios por encima de las escenas (por ejemplo: frmMensaje).

    public Engine() {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
    }

    private void close() {
        window.close();
    }

    private void loop() {
        Time.initTime();

        while (prgRun) {
            glfwPollEvents();

            if (!window.isMinimized()) {
                glClearColor(currentScene.getBackground().getRed(), currentScene.getBackground().getGreen(), currentScene.getBackground().getBlue(), 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                if(deltaTime >= 0) {
                    render();
                }

                glfwSwapBuffers(window.getWindow());

                Time.updateTime();
            }

            // Si hay algo para enviar, lo enviamos
            SocketConnection.getInstance().flushBuffer();

            // lo mismo para el handle data, leemos lo que nos envio el servidor.
            SocketConnection.getInstance().readData();
        }
    }

    public void start() {
        gameLoopThread.start();
    }

    public void init(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        this.window = Window.get();
        this.window.initialize();

        Surface.get().initialize();
        GameData.initialize();
        this.bindKeys = BindKeys.get();

        changeScene(INTRO_SCENE);
    }

    private void changeScene(SceneType scene) {
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
        if (KeyListener.isKeyPressed(bindKeys.getBindedKey(E_KeyType.mKeyExitGame))) {
            closeClient();
        }

        // Check change screen
        if (!currentScene.isVisible()) {
            changeScene(currentScene.getChangeScene());
        }

        currentScene.mouseEvents();
        currentScene.keyEvents();
        currentScene.render();

        // dibujado de formularios por encima de las escenas!
        if(!forms.isEmpty()) {
            for (Form frm : forms) {
                if (frm.isVisible()) {
                    frm.checkButtons();
                    frm.render();
                } else {
                    forms.remove(frm);
                    break;
                }
            }
        }
    }

    public static void closeClient() {
        prgRun = false;
    }

    @Override
    public void run() {
        init();
        loop();
        close();
    }
}
