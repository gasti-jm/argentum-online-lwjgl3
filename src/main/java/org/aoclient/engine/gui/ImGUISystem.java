package org.aoclient.engine.gui;

import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.console.ImGuiFonts;
import org.aoclient.engine.gui.forms.Form;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Sistema centralizado para la gestion de interfaces graficas de usuario basado en {@code ImGui}.
 * <p>
 * Se encarga de inicializar, configurar y renderizar todos los elementos de la interfaz de usuario utilizando la biblioteca
 * <i>Dear ImGui</i> adaptada para {@code LWJGL3}.
 * <p>
 * Administra el ciclo de vida de las distintas ventanas y formularios de la interfaz, permitiendo mostrar, ocultar y gestionar
 * componentes de UI como dialogos, menus, paneles y otros elementos interactivos. Mantiene una coleccion de formularios activos y
 * coordina su renderizado en cada fotograma.
 * <p>
 * Proporciona funcionalidad para la configuracion de estilos, fuentes y comportamientos de la interfaz, y maneja la
 * sincronizacion entre los eventos de entrada (teclado y raton) entre el sistema de ventanas GLFW y los componentes de ImGui,
 * asegurando una respuesta coherente de la interfaz.
 */

public enum ImGUISystem {

    INSTANCE;

    // LWJGL3 rendered itself (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    // Cursores del mouse proporcionados por GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // Those are used to track window size properties
    private final int[] winWidth = new int[1];
    private final int[] winHeight = new int[1];
    private final int[] fbWidth = new int[1];
    private final int[] fbHeight = new int[1];

    // For mouse tracking
    private final double[] mousePosX = new double[1];
    private final double[] mousePosY = new double[1];

    // arreglo de ventanas gui
    private final List<Form> frms = new ArrayList<>();
    
    private final Window window = Window.INSTANCE;

    public void init() {
        ImGui.createContext();

        /*
          ImGui proporciona 3 esquemas de color diferentes para diseñar. Usaremos el clásico aquí.
          Pruebe otros con los métodos ImGui.styleColors().
         */
        ImGui.styleColorsClassic();

        // Iniciamos la configuracion de ImGuiIO
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("resources/gui.ini"); // Guardamos en un archivo .ini
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navegacion con el teclado
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Cursores del mouse para mostrar al cambiar el tamaño de las ventanas, etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");
        io.setBackendRendererName("imgui_java_impl_lwjgl");

        // Teclado y raton.
        setKeyboardMapping(io);
        setMouseMapping();
        setCallbacks(io);

        // Configuracion de Fonts
        loadImGUIFonts(io);

        // Iniciamos ImGUI en OpenGL
        imGuiGl3.init();
    }

    private void setKeyboardMapping(ImGuiIO io) {
        final int[] keyMap = new int[ImGuiKey.COUNT];

        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        //keyMap[ImGuiKey.Enter]          = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;

        io.setKeyMap(keyMap);
    }

    private void setMouseMapping() {
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    }

    private void setCallbacks(ImGuiIO io) {
        glfwSetCharCallback(window.getWindow(), (w, c) -> {
            if (c != GLFW_KEY_DELETE) io.addInputCharacter(c);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(window.getWindow(), s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                return glfwGetClipboardString(window.getWindow());
            }
        });
    }

    private void loadImGUIFonts(ImGuiIO io) {
        final ImFontAtlas fontAtlas = io.getFonts();

        // Agregamos la tipografia por defecto, que es 'ProggyClean.ttf, 13px'
        fontAtlas.addFontDefault();

        // La creación de ImFontConfig asignará memoria nativa...
        final ImFontConfig fontConfig = new ImFontConfig();

        // Todas las fuentes agregadas mientras este modo está activado se fusionarán con la fuente agregada anteriormente
        fontConfig.setMergeMode(true);
        fontConfig.setPixelSnapH(true);
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());


        fontConfig.setMergeMode(false);
        fontConfig.setPixelSnapH(false);

        fontConfig.setRasterizerMultiply(1.2f);

        ImGuiFonts.fontRegular      = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Regular.ttf", 13);
        ImGuiFonts.fontBold         = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Bold.ttf", 13);
        ImGuiFonts.fontItalic       = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Italic.ttf", 13);
        ImGuiFonts.fontBoldItalic   = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-BoldItalic.ttf", 13);

        fontConfig.destroy();
    }

    public void destroy() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    public void renderGUI() {
        final ImGuiIO io = ImGui.getIO();
        if (io.getDeltaTime() <= 0) return;

        // Get window size properties and mouse position
        glfwGetWindowSize(window.getWindow(), winWidth, winHeight);
        glfwGetFramebufferSize(window.getWindow(), fbWidth, fbHeight);
        glfwGetCursorPos(window.getWindow(), mousePosX, mousePosY);

        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale((float) fbWidth[0] / winWidth[0], (float) fbHeight[0] / winHeight[0]);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // ACA HAY QUE LABURAR CON LOS CURSORES.
        if (window.isCursorCrosshair())
            if(options.isCursorGraphic()) {
                Window.INSTANCE.setCursorGraphic(true);
            } else {
                glfwSetCursor(window.getWindow(), glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR));
            }
        else {
            if(options.isCursorGraphic()) {
                Window.INSTANCE.setCursorGraphic(false);
            } else {
                glfwSetCursor(window.getWindow(), mouseCursors[ImGui.getMouseCursor()]);
                glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        }

        // IMPORTANT!!
        // Any Dear ImGui code SHOULD go between NewFrame()/Render() methods
        ImGui.newFrame();
        renderFrms();
        ImGui.render();

        // After ImGui#render call we provide draw data into LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void renderFrms() {

        for (int i = 0; i < frms.size(); i++)
            frms.get(i).render();

        ImGui.showDemoWindow();
    }

    public void show(Form frm) {
        boolean exits = false;
        // esta abierto?
        for (int i = 0; i < frms.size(); i++) {
            if (frms.get(i).getClass().getSimpleName().equals(frm.getClass().getSimpleName())) {
                frms.set(i, frm); // bueno remplazamos su contenido (esto si es que tenemos un frmMessage).
                exits = true;
                break;
            }
        }
        if (!exits) addFrm(frm);
    }

    public boolean isFormVisible(String fromClass) {
        boolean visible = false;
        for (Form frm : frms) {
            if (frm.getClass().getSimpleName().equals(fromClass)) {
                visible = true;
                break;
            }
        }
        return visible;
    }

    /**
     * Checkea si el frm solicitiado es el ultimo de nuestro array de frms.
     */
    public boolean isMainLast() {
        if (frms.size() <= 0) return false;


        return frms.get(frms.size() - 1).getClass().getSimpleName().equals("FMain");
    }


    public void deleteFrmArray(Form frm) {
        frms.remove(frm);
    }

    public void addFrm(Form e) {
        frms.add(e);
    }

    public void closeAllFrms() {
        frms.clear();
    }

    /**
     * Devuelve una copia de la lista de formularios activos.
     */
    public java.util.List<Form> getActiveForms() {
        return new java.util.ArrayList<>(frms);
    }
}
