package org.aoclient.engine;

import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.listeners.MouseListener;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Esta es la clase de nuestra Ventana GLFW, donde inicializa una nueva ventana y inicializa y crea nuestro contexto de
 * OpenGL y OpenAL.
 */
public final class Window {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private static Window instance;

    private long window;

    private long audioContext;
    private long audioDevice;

    private final String title;
    private final int width, height;

    /**
     * @desc: Constructor privado por singleton.
     */
    private Window() {
        this.title = "Argentum Online";
        this.width = SCREEN_WIDTH;
        this.height = SCREEN_HEIGHT;
    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static Window get() {
        if (instance == null) {
            instance = new Window();
        }

        return instance;
    }

    /**
     * @desc: Inicializamos nuestro contexto GLFW (ventana) con sus sistemas (OpenGL y OpenAL)
     */
    public void initialize() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();

        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        //glfwGetPrimaryMonitor()// permite pantalla completa. (PRIMER PARAMETRO NULL)
        // Create the window
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(window, KeyListener::keyCallback);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
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


        loadIcon();

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Disable v-sync
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);

        // Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            throw new IllegalStateException("Audio library not supported.");
        }

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glViewport(0, 0, width, height);
        glOrtho(0, width, height, 0, 1, -1);

        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glEnable(GL_ALPHA);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
    }

    /**
     * @desc: Destruye el contexto de LWJGL3 para el cierre del programa.
     */
    public void close() {
        // Destroy the audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Carga una imagen y la agrega como icono a nuestra ventana GLFW.
     */
    private void loadIcon() {
        try(MemoryStack stack = MemoryStack.stackPush()){
            final IntBuffer ch       = stack.mallocInt(1), w = stack.mallocInt(1), h = stack.mallocInt(1);
            final ByteBuffer imgBuff = STBImage.stbi_load("./resources/icon.png", w, h, ch, 4);

            if(imgBuff == null) return;

            GLFWImage image             = GLFWImage.malloc();
            GLFWImage.Buffer imageBf    = GLFWImage.malloc(1);

            image.set(w.get(), h.get(), imgBuff);
            imageBf.put(0, image);
            glfwSetWindowIcon(window, imageBf);
        }
    }

    /**
     * @desc Minimiza nuestra ventana
     */
    public void minimizar(){
        glfwIconifyWindow(window);
    }

    /**
     *
     * @return True si la ventana esta minimizada, caso contrario falso.
     */
    public boolean isMinimized() {
        return glfwGetWindowAttrib(window, GLFW_ICONIFIED) == GLFW_TRUE;
    }

    /**
     *
     * @return Getter del atributo Window (Contexto de nuestra ventana)
     */
    public long getWindow() {
        return this.window;
    }

    /**
     *
     * @return Getter del atributo width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return Getter del atributo height
     */
    public int getHeight() {
        return height;
    }

}
