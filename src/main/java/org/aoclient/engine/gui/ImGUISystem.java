package org.aoclient.engine.gui;

import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImInt;
import org.aoclient.engine.window.Window;
import org.aoclient.engine.game.console.ImGuiFonts;
import org.aoclient.engine.gui.forms.Form;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.engine.utils.LaunchOptions.CLIENT_DEBUG;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glGenTextures;

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


public enum ImGUISystem implements I_ImGuiSystem {
    INSTANCE;

    // LWJGL3 rendered itself (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

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

    @Override
    public void init() {
        ImGui.createContext();

        /*
          ImGui proporciona 3 esquemas de color diferentes para diseñar. Usaremos el clásico aquí.
          Pruebe otros con los métodos ImGui.styleColors().
         */
        ImGui.styleColorsClassic();

        // Iniciamos la configuracion de ImGuiIO
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);
        //io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navegacion con el teclado
        io.setConfigFlags(0);
        io.setConfigDebugHighlightIdConflicts(false);
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Cursores del mouse para mostrar al cambiar el tamaño de las ventanas, etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");
        io.setBackendRendererName("imgui_java_impl_lwjgl");

        // Configuracion de Fonts
        loadImGUIFonts(io);

        // Iniciamos GLFW (con 'true' instala todos los callbacks de teclado y mouse automáticamente)
        imGuiGlfw.init(window.getWindow(), true);
        // Iniciamos ImGUI en OpenGL
        imGuiGl3.init(window.getGlslVersion());
    }

    private void loadImGUIFonts(ImGuiIO io) {
        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setRasterizerMultiply(1.2f);

        // Nota: Si quieres aplicar fontConfig a tus fuentes, pásalo como 3er parámetro:
        ImGuiFonts.fontRegular    = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Regular.ttf", 13, fontConfig);
        ImGuiFonts.fontBold       = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Bold.ttf", 13, fontConfig);
        ImGuiFonts.fontItalic     = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-Italic.ttf", 13, fontConfig);
        ImGuiFonts.fontBoldItalic = fontAtlas.addFontFromFileTTF("resources/fonts/LiberationSans-BoldItalic.ttf", 13, fontConfig);

        fontConfig.destroy();

        // Construir la textura y subirla a OpenGL
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        // Esta línea es la que marca TexIsBuilt = true
        ByteBuffer pixels = fontAtlas.getTexDataAsRGBA32(width, height);

        int fontTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        fontAtlas.setTexID(fontTexture);
    }

    public void destroy() {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
    }

    @Override
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

        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();

        ImGui.newFrame();
        renderFrms();
        ImGui.render();

        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void renderFrms() {
        for (int i = 0; i < frms.size(); i++)
            frms.get(i).render();

        if (CLIENT_DEBUG)  ImGui.showDemoWindow();
    }

    @Override
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
