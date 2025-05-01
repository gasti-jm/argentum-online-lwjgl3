package org.aoclient.engine;

import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.scenes.*;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.Time;
import org.aoclient.network.SocketConnection;
import org.lwjgl.Version;
import org.tinylog.Logger;

import static org.aoclient.engine.Sound.playMusic;
import static org.aoclient.engine.scenes.SceneType.INTRO_SCENE;
import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

/**
 * <p>
 * Clase principal del motor grafico que se encarga  de inicializar, ejecutar y finalizar todas las funcionalidades principales
 * del cliente.
 * <p>
 * {@code Engine} gestiona el ciclo principal del juego, inicializando componentes criticos como la ventana, el sistema de sonido
 * {@code OpenAL}, el motor de renderizado {@code OpenGL} y el sistema de interfaz grafica mediante {@code ImGUI}.
 * <p>
 * El motor se encarga de administrar las diferentes escenas del juego, permitiendo transiciones entre
 * {@link org.aoclient.engine.scenes.IntroScene}, {@link org.aoclient.engine.scenes.MainScene}, y {@link GameScene}. Tambien
 * coordina la renderizacion de los elementos visuales y el procesamiento de eventos del teclado y raton en cada fotograma.
 * <p>
 * Ademas, se encarga de la gestion de recursos del sistema, asegurando la correcta inicializacion y liberacion de memoria al
 * iniciar o cerrar la aplicacion.
 */

public final class Engine {

    private static boolean prgRun = true;
    private Window window;
    private ImGUISystem guiSystem;
    private Scene currentScene;

    public Engine() {

    }

    /**
     * @desc: Funcion publica y estatica que permite ser utilizada en cualquier parte del programa, basicamente desactiva nuestro
     * booleano del MainLoop para que se cierre el juego.
     */
    public static void closeClient() {
        options.saveOptions();
        prgRun = false;
    }

    /**
     * Cierra nuestro motor grafico, para eso se llama al close de nuestra ventana, ya que tiene todo el contexto del motor.
     */
    private void close() {
        Sound.clearSounds();
        Sound.clearMusics();

        guiSystem.destroy();
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

                if (deltaTime >= 0) render();

                glfwSwapBuffers(window.getWindow());
                Time.updateTime();
            }

            // Si hay algo para enviar, lo enviamos
            SocketConnection.getInstance().flushBuffer();

            // lo mismo para el handle data, leemos lo que nos envio el servidor.
            SocketConnection.getInstance().read();
        }
    }

    /**
     * @desc: Inicia nuestro motor grafico con su ventana, gestor de texturas (surface), inits (gamedata) y escenas.
     */
    public void init() {
        Logger.info("Starting LWJGL {} !", Version.getVersion());
        Logger.info("Running on {} / v{} [{}]",
                System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));

        Logger.info("Java version: {}", System.getProperty("java.version"));

        GameData.initialize();

        window = Window.get();
        window.initialize();

        guiSystem = ImGUISystem.get();
        guiSystem.init();

        Surface.get().initialize();
        BindKeys bindKeys = BindKeys.get();

        changeScene(INTRO_SCENE);
        playMusic("intro.ogg");
    }

    /**
     * @param scene: SceneType es un enumerador definido en el package de scenes, bascicamente recibe un tipo de este enumerador y
     *               segun el elegido realiza el cambio de escena.
     * @desc: funcion que permite cambiar nuestro atributo de escena y cambiar de una a otra.
     */
    private void changeScene(SceneType scene) {
        switch (scene) {
            case INTRO_SCENE:
                currentScene = new IntroScene();
                break;
            case GAME_SCENE:
                currentScene = new GameScene();
                break;
            case MAIN_SCENE:
                currentScene = new MainScene();
                break;
        }
        currentScene.init();
    }

    /**
     * @desc: Renderizado general
     * <p>
     * Luego checkea si nuestra escena es visible, todas las escenas guardan un atributo de la posible escena en la que deba ir
     * cada una. Por ejemplo: En la escena de "IntroScene.java" tiene guardado para que su siguente escena sea la de la clase
     * "MainScene" donde se va mostrar el "frmConectar". Si estas escenas no son visibles, quiere decir que estan listas para ser
     * cambiadas a otra, por eso se llama a la funcion "changeScene".
     * <p>
     * Por ultimo, dibuja la escena en la que estemos y renderiza nuestra GUI del framework "Dear ImGUI".
     */
    private void render() {
        // Check change screen
        if (!currentScene.isVisible()) changeScene(currentScene.getChangeScene());
        currentScene.mouseEvents();
        currentScene.keyEvents();
        currentScene.render();
        guiSystem.renderGUI();
    }

    /**
     * @desc: Inicia nuestro motor grafico.
     */
    public void start() {
        init();
        loop();
        close();
    }

}
