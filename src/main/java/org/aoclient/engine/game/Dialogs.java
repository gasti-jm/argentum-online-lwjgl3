package org.aoclient.engine.game;

import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.game.models.Character.lastChar;
import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.Time.deltaTime;

public class Dialogs {
    private static float timerDialogs = 1.0f;

    public static void removeDialog(final int charIndex) {
        if(charList[charIndex].getDialog_life() > 0) {
            charList[charIndex].setDialog("");
            charList[charIndex].setDialog_life(0);
            charList[charIndex].setDialog_offset_counter_y(0);
        }
    }

    public static void removeAllDialogs() {
        for (int i = 1; i <= lastChar; i++) {
            if(!charList[i].getDialog().isEmpty()) {
                charDialogSet(i, "", new RGBColor(1,1,1), 0, 0);
            }
        }
    }

    public static void charDialogSet(final int charIndex, final String charDialog, final RGBColor color, final int charDialogLife, final int fontIndex) {
        charList[charIndex].setDialog(charDialog);
        charList[charIndex].setDialog_color(color);
        charList[charIndex].setDialog_life(charDialogLife);
        charList[charIndex].setDialog_font_index(fontIndex);
        charList[charIndex].setDialog_offset_counter_y(0);
    }

    public static void updateDialogs() {
        // paso 1 seg?
        if (timerDialogs <= 0) {
            timerDialogs = 1.0f;

            for (int i = 1; i <= lastChar; i++) {
                if(!charList[i].getDialog().isEmpty()) {
                    charList[i].setDialog_life(charList[i].getDialog_life() - 1);
                    if (charList[i].getDialog_life() <= 0) {
                        charList[i].setDialog("");
                        charList[i].setDialog_color(new RGBColor(1,1,1));
                        charList[i].setDialog_life(0);
                        charList[i].setDialog_offset_counter_y(0);
                        charList[i].setDialog_scroll(false);
                    }
                }
            }
        }

        timerDialogs -= deltaTime;
    }
}
