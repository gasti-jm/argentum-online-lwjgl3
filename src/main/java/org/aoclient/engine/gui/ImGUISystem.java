package org.aoclient.engine.gui;

import imgui.*;
import imgui.callbacks.ImStrConsumer;
import imgui.callbacks.ImStrSupplier;
import imgui.enums.*;
import imgui.gl3.ImGuiImplGl3;
import org.aoclient.engine.Window;
import org.aoclient.engine.gui.forms.Form;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.glfw.GLFW.*;


public class ImGUISystem {
    // Unico objeto del sistema de GUI.
    private static ImGUISystem instance;

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
    private List<Form> frms = new ArrayList<>();


    // Local variables for application goes here
    private final String imguiDemoLink = "https://raw.githubusercontent.com/ocornut/imgui/v1.75/imgui_demo.cpp"; // Link to put into clipboard
    private final byte[] testPayload = "Test Payload".getBytes(); // Test data for payload. Should be represented as raw byt array.
    private String dropTargetText = "Drop Here";
    private float[] backgroundColor = {0.5f, 0, 0}; // To modify background color dynamically
    private int clickCount = 0;
    private final ImString resizableStr = new ImString(5);
    private final ImBool showDemoWindow = new ImBool();
    private ImVec2 windowSize = new ImVec2(); // Vector to store "Custom Window" size
    private ImVec2 windowPos = new ImVec2(); // Vector to store "Custom Window" position


    private ImGUISystem() {

    }

    public static ImGUISystem get() {
        if (instance == null) {
            instance = new ImGUISystem();
        }

        return instance;
    }

    public void init() {
        ImGui.createContext();

        /*
          ImGui proporciona 3 esquemas de color diferentes para diseñar. Usaremos el clásico aquí.
          Pruebe otros con los métodos ImGui.styleColors().
         */
        ImGui.styleColorsClassic();

        // Iniciamos la configuracion de ImGuiIO
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("resources/inits/gui.ini"); // Guardamos en un archivo .ini
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
        final int[] keyMap              = new int[ImGuiKey.COUNT];

        keyMap[ImGuiKey.Tab]            = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow]      = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow]     = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow]        = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow]      = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp]         = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown]       = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home]           = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End]            = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert]         = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete]         = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace]      = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space]          = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter]          = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape]         = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter]    = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A]              = GLFW_KEY_A;
        keyMap[ImGuiKey.C]              = GLFW_KEY_C;
        keyMap[ImGuiKey.V]              = GLFW_KEY_V;
        keyMap[ImGuiKey.X]              = GLFW_KEY_X;
        keyMap[ImGuiKey.Y]              = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z]              = GLFW_KEY_Z;

        io.setKeyMap(keyMap);
    }

    private void setMouseMapping() {
        mouseCursors[ImGuiMouseCursor.Arrow]        = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput]    = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll]    = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS]     = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW]     = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW]   = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE]   = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand]         = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed]   = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    }

    private void setCallbacks(ImGuiIO io) {
        glfwSetCharCallback(Window.get().getWindow(), (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(Window.get().getWindow(), s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                return glfwGetClipboardString(Window.get().getWindow());
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

        // We merge font loaded from resources with the default one. Thus we will get an absent cyrillic glyphs
        //fontAtlas.addFontFromMemoryTTF(loadFromResources("basis33.ttf"), 16, fontConfig);

        // Disable merged mode and add all other fonts normally
        //fontConfig.setMergeMode(false);
        //fontConfig.setPixelSnapH(false);

        // ------------------------------
        // Fonts from file/memory example

        //fontConfig.setRasterizerMultiply(1.2f); // This will make fonts a bit more readable

        // We can add new fonts directly from file
        //fontAtlas.addFontFromFileTTF("src/test/resources/DroidSans.ttf", 13, fontConfig);
        //fontAtlas.addFontFromFileTTF("src/test/resources/DroidSans.ttf", 14, fontConfig);

        // Or directly from memory
        //fontConfig.setName("Roboto-Regular.ttf, 13px"); // This name will be displayed in Style Editor
        //fontAtlas.addFontFromMemoryTTF(loadFromResources("Roboto-Regular.ttf"), 13, fontConfig);
        //fontConfig.setName("Roboto-Regular.ttf, 14px"); // We can apply a new config value every time we add a new font
        //fontAtlas.addFontFromMemoryTTF(loadFromResources("Roboto-Regular.ttf"), 14, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more
    }

    public void destroy() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    public void renderGUI() {
        // Get window size properties and mouse position
        glfwGetWindowSize(Window.get().getWindow(), winWidth, winHeight);
        glfwGetFramebufferSize(Window.get().getWindow(), fbWidth, fbHeight);
        glfwGetCursorPos(Window.get().getWindow(), mousePosX, mousePosY);

        // IMPORTANT!!
        // We SHOULD call those methods to update ImGui state for current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale((float) fbWidth[0] / winWidth[0], (float) fbHeight[0] / winHeight[0]);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(Window.get().getWindow(), mouseCursors[imguiCursor]);
        glfwSetInputMode(Window.get().getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        // IMPORTANT!!
        // Any Dear ImGui code SHOULD go between NewFrame()/Render() methods
        ImGui.newFrame();

        //this.showUi();
        showFrms();

        ImGui.render();

        // After ImGui#render call we provide draw data into LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.render(ImGui.getDrawData());
    }

    private void showUi() {
        ImGui.setNextWindowSize(810, 605, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        ImGui.begin("Conectar", ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);  // Start Custom window

        // Example of how to draw an image in the bottom-right corner of the window
        ImGui.getWindowSize(windowSize);
        ImGui.getWindowPos(windowPos);
        final float xPoint = windowPos.x + windowSize.x;
        final float yPoint = windowPos.y + windowSize.y;

        //ImGui.image(backgroundImage, 800, 600);
        //ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, 800, 600);

        // Simple checkbox to show demo window
        //ImGui.checkbox("Show demo window", showDemoWindow);
        //ImGui.separator();

        // Drag'n'Drop functionality
        ImGui.button("Drag me");
        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload("payload_type", testPayload, testPayload.length);
            ImGui.text("Drag started");
            ImGui.endDragDropSource();
        }
        ImGui.sameLine();
        ImGui.text(dropTargetText);
        if (ImGui.beginDragDropTarget()) {
            final byte[] payload = ImGui.acceptDragDropPayload("payload_type");
            if (payload != null) {
                dropTargetText = new String(payload);
            }
            ImGui.endDragDropTarget();
        }

        // Color picker
        ImGui.alignTextToFramePadding();
        ImGui.text("Background color:");
        ImGui.sameLine();
        ImGui.colorEdit3("##click_counter_col", backgroundColor, ImGuiColorEditFlags.NoInputs | ImGuiColorEditFlags.NoDragDrop);

        // Simple click counter
        if (ImGui.button("Click")) {
            clickCount++;
        }
        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
        }
        ImGui.sameLine();
        ImGui.text("Count: " + clickCount);

        ImGui.separator();



        // Input field with auto-resize ability
        ImGui.text("You can use text inputs with auto-resizable strings!");
        ImGui.inputText("Resizable input", resizableStr, ImGuiInputTextFlags.CallbackResize);
        ImGui.text("text len:");
        ImGui.sameLine();
        ImGui.textColored(.12f, .6f, 1, 1, Integer.toString(resizableStr.getLength()));
        ImGui.sameLine();
        ImGui.text("| buffer size:");
        ImGui.sameLine();
        ImGui.textColored(1, .6f, 0, 1, Integer.toString(resizableStr.getBufferSize()));

        ImGui.separator();
        ImGui.newLine();

        // Link to the original demo file
        ImGui.text("Consider to look the original ImGui demo: ");
        ImGui.setNextItemWidth(500);
        ImGui.textColored(0, .8f, 0, 1, imguiDemoLink);
        ImGui.sameLine();
        if (ImGui.button("Copy")) {
            ImGui.setClipboardText(imguiDemoLink);
        }

        ImGui.end();  // End Custom window

        if (showDemoWindow.get()) {
            ImGui.showDemoWindow(showDemoWindow);
        }
    }

    private void showFrms() {
        for(int i = 0; i < frms.size(); i++) {
            frms.get(i).render();
        }

        ImGui.showDemoWindow();
    }

    public void checkAddOrChange(String formName, Form frm) {
        boolean exits = false;

        for(int i = 0; i < frms.size(); i++) {
            if (frms.get(i).getFormName().equals(formName)) {
                frms.set(i, frm);
                exits = true;
                break;
            }
        }

        if (!exits) {
            addFrm(frm);
        }
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

}
