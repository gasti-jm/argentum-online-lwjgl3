package org.aoclient.engine.game;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.renderer.RGBColor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa una consola de texto para mostrar mensajes al usuario.
 * <p>
 * La consola mantiene un historial de mensajes con un limite maximo, eliminando automaticamente los mas antiguos cuando se
 * alcanza dicho limite. Permite personalizar los mensajes con diferentes colores y estilos para categorizar o resaltar la
 * informacion mostrada al jugador.
 * <p>
 * Esta consola es fundamental para la comunicacion unidireccional del sistema hacia el jugador, mostrando eventos importantes,
 * resultados de acciones, mensajes del servidor y otros datos relevantes durante la experiencia de juego.
 */

public enum Console {

    INSTANCE;

    private static final int MAX_SIZE_DATA = 500;
    private boolean autoScroll;
    private boolean scrollToBottom;
    private List<ConsoleData> data;

    /**
     * Constructor privado por singleton.
     */
    Console() {
        this.autoScroll = true;
        this.scrollToBottom = false;
        this.data = new ArrayList<>();
    }

    /**
     * Agrega un nuevo mensaje en la consola.
     */
    public void addMsgToConsole(String text, boolean bold, boolean italic, RGBColor color) {
        this.data.add(new ConsoleData().addData(text, color));
    }

    public void clearConsole() {
        this.data.clear();
    }

    /**
     * Dibujamos la consola (esta es una porcion de GUI del frmMain).
     */
    public void drawConsole() {

        // si tenemos 500 mensajes, vaciamos la consola.
        if (data.size() > MAX_SIZE_DATA)
            clearConsole();


        ImGui.setNextWindowPos(10, 24);
        ImGui.setNextWindowSize(555, 98, ImGuiCond.Once);
        ImGui.begin("console", ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoBackground
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoSavedSettings);

        ImGui.setCursorPos(5, 0);
        ImGui.beginChild("ScrollingRegion", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar);

        // recorremos cada item y le asignamos un color y lo dibujamos.
        for (ConsoleData item : data) {
            ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(item.color.getRed(), item.color.getGreen(), item.color.getBlue(), 1f));
            ImGui.textUnformatted(item.consoleText);
            ImGui.popStyleColor();
        }


        if (scrollToBottom || (autoScroll && ImGui.getScrollY() >= ImGui.getScrollMaxY())) ImGui.setScrollHereY(1.0f);

        scrollToBottom = false;

        ImGui.endChild();
        ImGui.end();
    }

    static class ConsoleData {

        String consoleText = new String("".getBytes(), StandardCharsets.UTF_8);
        RGBColor color = new RGBColor(1f, 1f, 1f);

        ConsoleData addData(String text, RGBColor color) {
            this.consoleText += text;
            this.color = color;
            return this;
        }

    }

}
