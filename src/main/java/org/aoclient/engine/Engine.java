package org.aoclient.engine;

import org.aoclient.engine.audio.Sound;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.listeners.KeyHandler;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.BatchRenderer;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.scenes.*;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.Time;
import org.aoclient.network.Connection;
import org.lwjgl.Version;
import org.tinylog.Logger;

import static org.aoclient.engine.audio.Sound.playMusic;
import static org.aoclient.engine.scenes.SceneType.INTRO_SCENE;
import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

/**
 * Clase principal que representa el motor grafico del proyecto.
 * <p>
 * Esta clase gestiona la inicializacion, ejecucion y finalizacion de los componentes esenciales del motor. Es responsable de
 * coordinar el renderizado, la logica principal, el manejo de eventos y la comunicacion con el servidor.
 * <p>
 * El ciclo de vida del motor incluye tres etapas principales: inicializacion de recursos mediante el metodo {@code init()},
 * ejecucion del bucle principal con {@code loop()} y cierre de recursos junto con la terminacion del programa a traves del metodo
 * {@code close()}.
 */

public final class Engine {

    /** Flag que indica si el programa esta corriendo. */
    private static boolean prgRun = true;
    /** Ventana principal del motor grafico. */
    private final Window window = Window.INSTANCE;
    /** Sistema de interfaz grafica de usuario. */
    private final ImGUISystem guiSystem = ImGUISystem.INSTANCE;
    /** Escena actual que esta siendo renderizada y actualizada en el motor. */
    private Scene currentScene;
    public static BatchRenderer batch;

    /**
     * Finaliza el cliente del motor grafico cerrando los recursos necesarios y deteniendo su ejecucion.
     * <p>
     * Este metodo garantiza que las opciones actuales sean almacenadas invocando el metodo {@code options.save()}.
     * Adicionalmente, ajusta el estado del programa a inactivo configurando {@code prgRun} a {@code false}.
     */
    public static void closeClient() {
        options.save();
        prgRun = false;
    }

    /**
     * Inicializa los componentes esenciales del motor grafico.
     */
    public void init() {
        Logger.info("Starting LWJGL {}!", Version.getVersion());
        Logger.info("Running on {} / v{} [{}]", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        Logger.info("Java version: {}", System.getProperty("java.version"));

        GameData.init();
        window.init();
        guiSystem.init();
        Surface.INSTANCE.init();
        batch = new BatchRenderer();

        changeScene(INTRO_SCENE);
        playMusic("intro.ogg");
    }

    /**
     * Metodo principal para iniciar el motor grafico.
     * <p>
     * Este metodo coordina el flujo principal de ejecucion del motor. Primero, inicializa los elementos necesarios como la
     * ventana, gestor de texturas, escenas, y otros componentes fundamentales llamando al metodo {@code init()}. A continuacion,
     * se ejecuta el bucle principal del juego mediante el metodo {@code loop()}, que gestiona los eventos, la logica, el
     * renderizado y la comunicacion con el servidor. Finalmente, se limpian y cierran los recursos utilizados llamando al metodo
     * {@code close()}.
     */
    public void start() {
        init();
        loop();
        close();
    }

    /**
     * Cierra los recursos y finaliza el funcionamiento del motor grafico.
     */
    private void close() {
        Sound.clearSounds();
        Sound.clearMusics();
        guiSystem.destroy();
        window.close();
    }

    /**
     * Metodo principal del ciclo de ejecucion del motor grafico.
     * <p>
     * Este metodo es el encargado de gestionar el flujo principal del renderizado y la logica del programa mientras este se
     * encuentra en ejecucion. Realiza las siguientes tareas:
     * <ul>
     * <li>Inicializa el tiempo al inicio del bucle llamando a {@code Time.initTime()}.
     * <li>Itera mientras el programa esta activo, verificando continuamente eventos de la ventana con {@code glfwPollEvents()}.
     * <li>Si la ventana no esta minimizada:
     * <ul>
     *  <li>Establece el color de fondo del renderizado basandose en los valores RGB de la escena actual.
     *  <li>Renderiza la escena actual si {@code deltaTime >= 0}.
     *  <li>Muestra el contenido renderizado actualizando los buffers con {@code glfwSwapBuffers}.
     *  <li>Actualiza los timers llamando a {@code Time.updateTime()}.
     * </ul>
     * <li>Resetea el estado de botones del raton con {@code MouseListener.resetReleasedButtons()}.
     * <li>Gestiona la comunicacion con el servidor enviando y recibiendo bytes utilizando
     * {@code SocketConnection.INSTANCE.write()} y {@code SocketConnection.INSTANCE.read()} respectivamente.
     * </ul>
     * <p>
     * Este bucle mantiene el motor grafico activo hasta que el estado de ejecucion del programa {@code prgRun} cambie a
     * {@code false}.
     */
    private void loop() {
        Time.initTime();

        while (prgRun) {
            glfwPollEvents();

            if (!window.isMinimized()) {
                glClearColor(currentScene.getBackground().getRed(), currentScene.getBackground().getGreen(), currentScene.getBackground().getBlue(), 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                if (deltaTime >= 0)
                    render();


                glfwSwapBuffers(window.getWindow());
                Time.updateTime();
            }

            MouseListener.resetReleasedButtons();

            // Depues de realizar cualquier accion, se comunica con el servidor para informarle de esta accion...

            // Si hay algo para enviar, lo envia (escribe lo que envia el cliente al servidor)
            Connection.INSTANCE.write();
            // Si hay algo para recibir, lo recibe (lee lo que recibe el cliente del servidor)
            Connection.INSTANCE.read();

        }
    }

    /**
     * Cambia la escena actual del juego a una nueva basada en el tipo de escena proporcionado.
     * <p>
     * Inicializa la nueva escena una vez que se ha creado.
     *
     * @param scene tipo de escena
     */
    private void changeScene(SceneType scene) {
        switch (scene) {
            case INTRO_SCENE -> currentScene = new IntroScene();
            case GAME_SCENE -> currentScene = new GameScene();
            case MAIN_SCENE -> currentScene = new MainScene();
        }
        currentScene.init();
    }

    /**
     * <b>Renderizado general</b>
     * <p>
     * Luego checkea si nuestra escena es visible, todas las escenas guardan un atributo de la posible escena en la que deba ir
     * cada una. Por ejemplo: En la escena de "IntroScene.java" tiene guardado para que su siguente escena sea la de la clase
     * "MainScene" donde se va mostrar el "frmConectar". Si estas escenas no son visibles, quiere decir que estan listas para ser
     * cambiadas a otra, por eso se llama a la funcion "changeScene".
     * <p>
     * Por ultimo, dibuja la escena en la que estemos y renderiza nuestra GUI del framework "Dear ImGUI".
     */
    private void render() {
        if (!currentScene.isVisible()) changeScene(currentScene.getChangeScene());

        batch.begin();
        currentScene.mouseEvents();
        currentScene.keyEvents();
        currentScene.render();
        batch.end();
        guiSystem.renderGUI();

        Sound.renderMusic();
        KeyHandler.update();
    }

}
