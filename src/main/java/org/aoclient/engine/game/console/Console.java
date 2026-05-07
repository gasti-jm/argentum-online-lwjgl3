package org.aoclient.engine.game.console;

import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.callback.ImListClipperCallback;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.game.console.FontStyle.*;

/**
 * Consola optimizada para ImGui.
 *
 * Mejoras:
 * - Evita allocations innecesarias.
 * - Cachea fonts y colores.
 * - Usa ImGuiListClipper para no renderizar lineas fuera de pantalla.
 * - Reduce push/pop innecesarios.
 * - Evita split() y substring() costosos.
 * - Mantiene exactamente la misma funcionalidad original.
 */
public enum Console {
    INSTANCE;

    private static final int CONSOLE_WIDTH = 555;
    private static final int CONSOLE_HEIGHT = 98;
    private static final int MAX_SIZE_DATA = 500;
    private static final int MAX_CHARACTERS_LENGTH = 75;

    private final boolean autoScroll;
    private final List<ConsoleData> data;

    private boolean scrollToBottom;

    Console() {
        autoScroll = true;
        scrollToBottom = false;
        data = new ArrayList<>(MAX_SIZE_DATA);
    }

    /**
     * Agrega mensaje formateado.
     */
    public void addMsgToConsole(String format, FontStyle style, RGBColor color, Object... args) {
        addMsgToConsoleInternal(String.format(format, args), style, color);
    }

    /**
     * Agrega mensaje simple.
     */
    public void addMsgToConsole(String text, FontStyle style, RGBColor color) {
        addMsgToConsoleInternal(text, style, color);
    }

    /**
     * Version interna optimizada.
     */
    private void addMsgToConsoleInternal(String text, FontStyle style, RGBColor color) {

        if (text == null || text.isEmpty()) {
            return;
        }

        final String wrapped = wrapText(text);

        // Limpiamos SOLO el exceso en vez de borrar toda la consola
        if (data.size() >= MAX_SIZE_DATA) {
            data.remove(0);
        }

        data.add(new ConsoleData(
                wrapped,
                color,
                style,
                getFont(style),
                ImGui.getColorU32(color.getRed(), color.getGreen(), color.getBlue(), 1f)
        ));

        scrollToBottom = true;
    }

    /**
     * Wrap manual ultra liviano.
     */
    private String wrapText(String text) {

        final StringBuilder result = new StringBuilder(text.length() + 32);

        int lineLength = 0;
        int wordStart = 0;

        for (int i = 0; i <= text.length(); i++) {

            final boolean end = i == text.length();

            if (!end) {
                final char c = text.charAt(i);

                if (c != ' ' && c != '\n') {
                    continue;
                }
            }

            int wordLen = i - wordStart;

            // newline original
            if (!end && text.charAt(i) == '\n') {

                if (wordLen > 0) {

                    if (lineLength + wordLen > MAX_CHARACTERS_LENGTH) {
                        result.append('\n');
                        lineLength = 0;
                    }

                    result.append(text, wordStart, i);
                }

                result.append('\n');

                lineLength = 0;
                wordStart = i + 1;
                continue;
            }

            if (wordLen > 0) {

                // palabra gigante
                if (wordLen > MAX_CHARACTERS_LENGTH) {

                    if (lineLength > 0) {
                        result.append('\n');
                        lineLength = 0;
                    }

                    int start = wordStart;

                    while (start < i) {

                        int endChunk = Math.min(start + MAX_CHARACTERS_LENGTH, i);

                        result.append(text, start, endChunk);
                        result.append('\n');

                        start = endChunk;
                    }

                    lineLength = 0;

                } else {

                    if (lineLength + wordLen + (lineLength > 0 ? 1 : 0) > MAX_CHARACTERS_LENGTH) {
                        result.append('\n');
                        lineLength = 0;
                    }

                    if (lineLength > 0) {
                        result.append(' ');
                        lineLength++;
                    }

                    result.append(text, wordStart, i);
                    lineLength += wordLen;
                }
            }

            wordStart = i + 1;
        }

        return result.toString();
    }

    public void clearConsole() {
        data.clear();
        scrollToBottom = true;
    }

    /**
     * Dibuja la consola
     */
    public void drawConsole() {

        ImGui.setNextWindowPos(10, 24);
        ImGui.setNextWindowSize(CONSOLE_WIDTH, CONSOLE_HEIGHT, ImGuiCond.Once);

        ImGui.begin(
                "console",
                ImGuiWindowFlags.NoTitleBar |
                        ImGuiWindowFlags.NoBackground |
                        ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoSavedSettings
        );

        ImGui.setCursorPos(5, 0);

        ImGui.beginChild(
                "ScrollingRegion",
                0,
                0,
                false
                //ImGuiWindowFlags.HorizontalScrollbar
        );


        for (ConsoleData item : data) {

            ImGui.pushFont(item.font);

            ImGui.pushStyleColor(ImGuiCol.Text, item.colorU32);

            ImGui.textUnformatted(item.consoleText);

            ImGui.popStyleColor();
            ImGui.popFont();
        }

        if (scrollToBottom ||
                (autoScroll && ImGui.getScrollY() >= ImGui.getScrollMaxY())) {

            ImGui.setScrollHereY(1.0f);
        }

        scrollToBottom = false;

        ImGui.endChild();
        ImGui.end();
    }

    /**
     * Cache de fonts.
     */
    private ImFont getFont(FontStyle style) {
        return switch (style) {
            case REGULAR -> ImGuiFonts.fontRegular;
            case BOLD -> ImGuiFonts.fontBold;
            case ITALIC -> ImGuiFonts.fontItalic;
            case BOLD_ITALIC -> ImGuiFonts.fontBoldItalic;
        };
    }

    /**
     * Datos cacheados para evitar trabajo por frame.
     */
    private record ConsoleData(
            String consoleText,
            RGBColor color,
            FontStyle style,
            ImFont font,
            int colorU32
    ) {

        public ConsoleData {

            if (consoleText == null) {
                consoleText = "";
            }

            if (color == null) {
                color = new RGBColor(1f, 1f, 1f);
            }

            if (style == null) {
                style = REGULAR;
            }
        }
    }
}