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
 * <p>
 * TODO Agregar italic y bold
 */

public enum Console {
    INSTANCE;

    public static final int CONSOLE_WIDTH = 555;
    public static final int CONSOLE_HEIGHT = 98;
    private static final int MAX_SIZE_DATA = 500;
    private static final int MAX_CHARACTERS_LENGTH = 75; // Maxima cantidad de caracteres en horizontal.
    private final boolean autoScroll;
    private final List<ConsoleData> data;
    private boolean scrollToBottom;

    Console() {
        autoScroll = true;
        scrollToBottom = false;
        data = new ArrayList<>();
    }

    /**
     * Agrega un nuevo mensaje en la consola.
     */
    public void addMsgToConsole(String text, boolean bold, boolean italic, RGBColor color) {
        StringBuilder resultado = new StringBuilder();

        for (String linea : text.split("\n")) {
            String[] palabras = linea.split(" ");
            StringBuilder lineaActual = new StringBuilder();

            for (String palabra : palabras) {
                if (lineaActual.length() + palabra.length() + 1 > MAX_CHARACTERS_LENGTH) {
                    // Si la palabra sola es muy larga, la cortamos igual
                    if (palabra.length() > MAX_CHARACTERS_LENGTH) {
                        if (lineaActual.length() > 0) {
                            resultado.append(lineaActual.toString().stripTrailing()).append("\n");
                            lineaActual.setLength(0);
                        }
                        int inicio = 0;
                        while (inicio < palabra.length()) {
                            int fin = Math.min(inicio + MAX_CHARACTERS_LENGTH, palabra.length());
                            resultado.append(palabra.substring(inicio, fin)).append("\n");
                            inicio = fin;
                        }
                    } else {
                        resultado.append(lineaActual.toString().stripTrailing()).append("\n");
                        lineaActual = new StringBuilder(palabra + " ");
                    }
                } else {
                    lineaActual.append(palabra).append(" ");
                }
            }

            // Agregar lo que quedó en la línea
            if (lineaActual.length() > 0) {
                resultado.append(lineaActual.toString().stripTrailing()).append("\n");
            }
        }

        data.add(new ConsoleData(resultado.toString(), color));

        // Activa el scroll hacia abajo cuando se agrega un mensaje
        scrollToBottom = true;
    }

    public void clearConsole() {
        data.clear();
        scrollToBottom = true;
    }

    /**
     * Dibuja la consola (esta es una porcion de GUI del frmMain).
     */
    public void drawConsole() {

        // Limpia la consola despues de 500 mensajes
        if (data.size() > MAX_SIZE_DATA) clearConsole();

        ImGui.setNextWindowPos(10, 24);
        ImGui.setNextWindowSize(CONSOLE_WIDTH, CONSOLE_HEIGHT, ImGuiCond.Once);
        ImGui.begin("console", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoSavedSettings);
        ImGui.setCursorPos(5, 0);
        ImGui.beginChild("ScrollingRegion", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar);

        // Itera cada item y le asignamos un color y lo dibujamos.
        for (ConsoleData item : data) {
            ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(item.color.getRed(), item.color.getGreen(), item.color.getBlue(), 1f));
            ImGui.textUnformatted(item.consoleText);
            ImGui.popStyleColor();
        }

        // Hace scroll hacia abajo si se solicita o si el autoScroll esta activado y ya esta cerca del final
        if (scrollToBottom || (autoScroll && ImGui.getScrollY() >= ImGui.getScrollMaxY())) ImGui.setScrollHereY(1.0f);

        scrollToBottom = false;

        ImGui.endChild();
        ImGui.end();
    }

    private record ConsoleData(String consoleText, RGBColor color) {
        public ConsoleData {
            // Validaciones si son necesarias
            if (consoleText == null) consoleText = "";
            if (color == null) color = new RGBColor(1f, 1f, 1f);
        }
    }

}
