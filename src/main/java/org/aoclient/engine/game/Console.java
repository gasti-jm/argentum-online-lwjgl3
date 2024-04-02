package org.aoclient.engine.game;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.renderer.RGBColor;

import java.nio.charset.StandardCharsets;

public class Console {
    private static Console instance;

    private String consoleText;
    private boolean autoScroll;
    private boolean scrollToBottom;

    /**
     * Constructor privado por singleton.
     */
    private Console() {
        this.consoleText        = new String("".getBytes(), StandardCharsets.UTF_8);
        this.autoScroll         = true;
        this.scrollToBottom     = false;
    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static Console get() {
        if (instance == null) {
            instance = new Console();
        }

        return instance;
    }

    /**
     * Agrega un nuevo mensaje en la consola.
     */
    public void addMsgToConsole(String text, boolean bold, boolean italic, RGBColor color) {
        consoleText += text + "\n";
    }

    public void clearConsole() {
        consoleText = "";
    }

    /**
     * Dibujamos la consola (esta es una porcion de GUI del frmMain).
     */
    public void drawConsole() {
        ImGui.setNextWindowPos(10, 24);
        ImGui.setNextWindowSize(555, 98, ImGuiCond.Once);
        ImGui.begin("console", ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoBackground
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoSavedSettings);

        ImGui.setCursorPos(5, 0);
        ImGui.beginChild("ScrollingRegion", 0, 0, false,  ImGuiWindowFlags.HorizontalScrollbar);
        ImGui.textUnformatted(consoleText);

        if (scrollToBottom || (autoScroll && ImGui.getScrollY() >= ImGui.getScrollMaxY()))
                ImGui.setScrollHereY(1.0f);

        scrollToBottom = false;

        ImGui.endChild();
        ImGui.end();
    }

}
