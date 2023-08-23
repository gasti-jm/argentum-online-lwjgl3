package org.aoclient.engine.game;

import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.renderer.FontText.drawConsoleText;

public class Console {
    private static Console instance;
    private static final int POS_X = 12;
    private static final int POS_Y = 24;

    static class TextConsole {
        String text;
        boolean bold, italic;
        RGBColor color;

        public TextConsole(String text, boolean bold, boolean italic, RGBColor color) {
            this.text = text;
            this.bold = bold;
            this.italic = italic;
            this.color = color;
        }
    }

    private final List<TextConsole> dataConsole = new ArrayList<>();
    private int posList;

    private Console() {
        this.posList = 0;
    }

    public static Console get() {
        if (instance == null) {
            instance = new Console();
        }

        return instance;
    }

    public void addMessageToConsole(String text, boolean bold, boolean italic, RGBColor color) {
        if(dataConsole.size() > 100) {
            dataConsole.clear();
            posList = 0;
        }

        if(dataConsole.size() >= 7) {
            posList++;
        }

        dataConsole.add(new TextConsole(text, bold, italic, color));
    }

    public void drawConsole() {
        // Tenemos que mostrar siempre los ultimos.
        if (!dataConsole.isEmpty()) {
            for (int i = this.posList; i < dataConsole.size(); i++) {
                TextConsole data = dataConsole.get(i);
                drawConsoleText(data.text, POS_X, POS_Y + ((i - this.posList) * 12), data.color, 0, data.bold, data.italic);
            }
        }
    }


}
