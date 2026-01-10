package org.aoclient.engine;

import org.aoclient.engine.listeners.KeyHandler;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.utils.Platform;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.aoclient.engine.utils.GameData.options;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * Clase encargada de gestionar la ventana principal del juego y sus contextos graficos.
 * <p>
 * Esta clase implementa el patron singleton para proporcionar una unica instancia que maneja la ventana GLFW del juego,
 * inicializando y administrando tanto el contexto grafico (OpenGL) como el de audio (OpenAL).
 * <p>
 * Se encarga de configurar las propiedades de la ventana como resolucion, modo de pantalla completa, sincronizacion vertical, asi
 * como de establecer los callbacks para eventos del teclado y raton.
 * <p>
 * Proporciona funcionalidades para alternar entre modo ventana y pantalla completa, minimizar la ventana, y verificar su estado
 * actual. Al cerrar la aplicacion, esta clase se encarga de liberar correctamente todos los recursos asociados a los contextos de
 * OpenGL y OpenAL.
 * <p>
 * La clase mantiene las dimensiones constantes de la ventana (800x600) y gestiona el posicionamiento centrado en la pantalla del
 * usuario.
 */

public enum Window {

    INSTANCE;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private final String title;
    private final int width, height;
    private long window;
    private long audioContext;
    private long audioDevice;
    private boolean cursorCrosshair;

    private long cursorMainID;
    private long cursorCrosshairID;

    Window() {
        this.title = "Argentum Online Java";
        this.width = SCREEN_WIDTH;
        this.height = SCREEN_HEIGHT;
        this.cursorCrosshair = false;
    }

    /**
     * Inicializamos nuestro contexto GLFW (ventana) con sus sistemas (OpenGL y OpenAL)
     */
    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        // Configure GLFW
        glfwDefaultWindowHints();

        if (Platform.isMac()) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        } else {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        // Create the window
        window = glfwCreateWindow(this.width, this.height, this.title, options.isFullscreen() ? glfwGetPrimaryMonitor() : NULL, NULL);

        if (window == NULL) throw new IllegalStateException("Failed to create the GLFW window.");

        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(window, KeyHandler::keyCallback);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically



        // ========================================================================
        // Inicializacion de audio (OpenAL)
        // ========================================================================

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        // Verifica si el dispositivo se abrio correctamente
        if (audioDevice == NULL) {
            System.out.println("Could not open default audio device: " + defaultDeviceName);

            // Intenta con el primer dispositivo disponible
            String deviceList = alcGetString(0, ALC_DEVICE_SPECIFIER);
            if (deviceList != null && !deviceList.isEmpty()) {
                System.out.println("Trying with the first available device: " + deviceList);
                audioDevice = alcOpenDevice(deviceList);
            }

            // Si aun falla, intenta sin especificar dispositivo
            if (audioDevice == NULL) {
                System.out.println("Trying to open unspecified audio device...");
                audioDevice = alcOpenDevice((String) null);
            }

        }

        // Solo crea el contexto si tenemos un dispositivo valido
        if (audioDevice != NULL) {
            int[] attributes = {0};
            audioContext = alcCreateContext(audioDevice, attributes);

            if (audioContext == NULL) {
                System.out.println("The audio context could not be created");
                alcCloseDevice(audioDevice);
                audioDevice = NULL;
            } else {
                alcMakeContextCurrent(audioContext);

                ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
                ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

                if (!alCapabilities.OpenAL10) {
                    System.out.println("OpenAL 1.0 is not supported");
                    alcDestroyContext(audioContext);
                    alcCloseDevice(audioDevice);
                    audioDevice = NULL;
                    audioContext = NULL;
                }
            }
        } else System.out.println("Client running without audio!");

        // ========================================================================
        // Inicializacion de OpenGL
        // ========================================================================

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // En macOS, no se puede cargar el icono en glfw.
        if (!Platform.isMac()) {
            loadIcon();
        }

        if (options.isVsync()) glfwSwapInterval(1);
        else glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);

        // cargamos los cursores graficos!
        cursorMainID        = loadCursor("MAIN");
        cursorCrosshairID   = loadCursor("CAST");

        // set current cursor
        if(options.isCursorGraphic()) {
            glfwSetCursor(window, cursorMainID);
        }

        // Inicializa OpenGL
        GL.createCapabilities();

        glEnable(GL_ALPHA);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        glViewport(0,0, this.width, this.height);
    }

    public void setCursorGraphic(boolean cast) {
        if(!cast) {
            glfwSetCursor(window, cursorMainID);
        } else {
            glfwSetCursor(window, cursorCrosshairID);
        }
    }

    /**
     * Destruye el contexto de LWJGL3 para el cierre del programa.
     */
    public void close() {
        // Destroy the audio context solo si se inicializó
        if (audioContext != NULL) alcDestroyContext(audioContext);
        if (audioDevice != NULL) alcCloseDevice(audioDevice);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void toggleWindow() {
        if (options.isFullscreen()) glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, GLFW_DONT_CARE);
        else {
            glfwSetWindowMonitor(window, NULL,
                    0,
                    0,
                    width, height, GLFW_DONT_CARE);

            // Get the thread stack and push a new frame
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1); // int*
                IntBuffer pHeight = stack.mallocInt(1); // int*

                // Get the window size passed to glfwCreateWindow
                glfwGetWindowSize(window, pWidth, pHeight);

                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

                // Center the window
                glfwSetWindowPos(
                        window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            } // the stack frame is popped automatically
        }

        if (options.isVsync()) glfwSwapInterval(1);
        else glfwSwapInterval(0);

    }

    /**
     * Minimiza nuestra ventana
     */
    public void minimizar() {
        glfwIconifyWindow(window);
    }

    /**
     * @return True si la ventana esta minimizada, caso contrario falso.
     */
    public boolean isMinimized() {
        return glfwGetWindowAttrib(window, GLFW_ICONIFIED) == GLFW_TRUE;
    }

    public long getWindow() {
        return this.window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCursorCrosshair() {
        return cursorCrosshair;
    }

    public void setCursorCrosshair(boolean cursorCrosshair) {
        this.cursorCrosshair = cursorCrosshair;
    }

    /**
     * Verifica si el audio esta disponible en el contexto actual.
     *
     * @return true si el dispositivo de audio y el contexto de audio no son nulos, false en caso contrario
     */
    public boolean isAudioAvailable() {
        return audioDevice != NULL && audioContext != NULL;
    }

    /**
     * Carga una imagen y la agrega como icono a nuestra ventana GLFW.
     */
    private void loadIcon() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer ch = stack.mallocInt(1), w = stack.mallocInt(1), h = stack.mallocInt(1);
            final ByteBuffer imgBuff = STBImage.stbi_load("./resources/icon.png", w, h, ch, 4);

            if (imgBuff == null) return;

            GLFWImage image = GLFWImage.malloc();
            GLFWImage.Buffer imageBf = GLFWImage.malloc(1);

            image.set(w.get(), h.get(), imgBuff);
            imageBf.put(0, image);
            glfwSetWindowIcon(window, imageBf);
        }
    }

    /**
     * Carga un cursor grafico, por ahora no lo vamos a usar.
     */
    private long loadCursor(final String fileName) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer ch = stack.mallocInt(1), w = stack.mallocInt(1), h = stack.mallocInt(1);
            final ByteBuffer imgBuff = STBImage.stbi_load("./resources/" + fileName + ".png", w, h, ch, 4);

            if (imgBuff == null) return 0;

            GLFWImage image = GLFWImage.malloc();
            GLFWImage.Buffer imageBf = GLFWImage.malloc(1);

            image.set(w.get(), h.get(), imgBuff);
            imageBf.put(0, image);

            // the hotspot indicates the displacement of the sprite to the
            // position where mouse clicks are registered (see image below)
            final int hotspotX = 3;
            final int hotspotY = 6;

            // create custom cursor and store its ID
            return glfwCreateCursor(image, hotspotX, hotspotY);
        }
    }

}
