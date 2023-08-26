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

import java.awt.*;
import java.awt.event.KeyEvent;
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
    public static boolean capsState = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

    public Engine() {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
    }

    private void close() {
        window.close();
    }

    /**
     * @desc: Main loop del juego, se empieza a correr ni bien se inicia nuestro motor grafico.
     */
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

    /**
     * @desc: Inicia nuestro Thread principal.
     */
    public void start() {
        gameLoopThread.start();
    }

    /**
     * @desc: Inicia nuestro motor grafico con su ventana, gestor de texturas (surface), inits (gamedata) y escenas.
     */
    public void init(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        this.window = Window.get();
        this.window.initialize();

        Surface.get().initialize();
        GameData.initialize();
        this.bindKeys = BindKeys.get();

        changeScene(INTRO_SCENE);
    }

    /**
     * @desc: funcion que permite cambiar nuestro atributo de escena y cambiar de una a otra.
     *
     * @param: scene: SceneType es un enumerador definido en el package de scenes, bascicamente recibe un tipo de este
     *         enumerador y segun el elegido realiza el cambio de escena.
     */
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

    /**
     * @desc: Renderizado general, primero checkea algunas teclas claves que es para salir del juego con ESC y
     *        el cambio de mayusculas (Bloq mayus) para ser detectado en cualquier momento por el programa.
     *
     *        Luego checkea si nuestra escena es visible, todas las escenas guardan un atributo de la posible escena
     *        en la que deba ir cada una. Por ejemplo: En la escena de "IntroScene.java" tiene guardado para que su
     *        siguente escena sea la de la clase "MainScene" donde se va mostrar el "frmConectar".
     *        Si estas escenas no son visibles, quiere decir que estan listas para ser cambiadas a otra, por eso se
     *        llama a la funcion "changeScene".
     *
     *        Por ultimo, dibuja la escena en la que estemos y renderiza cualquier formulario que pueda estar por encima
     *        en cualquier escena. Por ejemplo un "frmMensaje".
     */
    private void render() {
        if (KeyListener.isKeyPressed(bindKeys.getBindedKey(E_KeyType.mKeyExitGame))) {
            closeClient();
        } else if(KeyListener.isKeyReadyForAction(GLFW_KEY_CAPS_LOCK)) {
            capsState = !capsState;
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

    /**
     * @desc: Funcion publica y estatica que permite ser utilizada en cualquier parte del programa, basicamente
     *        desactiva nuestro booleano del MainLoop para que se cierre el juego.
     */
    public static void closeClient() {
        prgRun = false;
    }

    /**
     * @desc: Funcion sobreescrita de la interface Runnable, permite ejecutar nuestro Thread principal y inicializar
     *        nuestro motor grafico.
     */
    @Override
    public void run() {
        init();
        loop();
        close();
    }
}
