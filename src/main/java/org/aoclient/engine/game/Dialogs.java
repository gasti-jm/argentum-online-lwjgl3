package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.scenes.Camera;

import static org.aoclient.engine.game.models.Character.lastChar;
import static org.aoclient.engine.renderer.FontRenderer.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.GameData.mapData;
import static org.aoclient.engine.utils.Time.deltaTime;

/**
 * Clase encargada de la gestion de dialogos y mensajes de texto que aparecen sobre los personajes.
 * <p>
 * Esta clase proporciona funcionalidad para crear, actualizar y eliminar los dialogos que aparecen flotando sobre los personajes.
 * Maneja tanto dialogos normales como dialogos especiales para representar golpes, daño y otros eventos.
 * <p>
 * Los dialogos tienen un ciclo de vida determinado, desapareciendo automaticamente despues de un periodo de tiempo establecido.
 * <p>
 * La clase se encarga de:
 * <ul>
 * <li>Mostrar mensajes de texto sobre los personajes
 * <li>Aplicar colores y estilos diferentes segun el tipo de dialogo
 * <li>Gestionar la aparicion y desvanecimiento de los textos
 * <li>Coordinar la posicion de los dialogos para que aparezcan correctamente sobre las cabezas de los personajes
 * <li>Eliminar dialogos individuales o todos los dialogos en pantalla
 * </ul>
 */

public class Dialogs {

    private static final RGBColor HIT_COLOR = new RGBColor(1, 0, 0);
    private static float timerDialogs = 1.0f;

    public static void removeDialog(final int charIndex) {
        if (charList[charIndex].getDialog_life() > 0) {
            charList[charIndex].setDialog("");
            charList[charIndex].setDialog_life(0);
            charList[charIndex].setDialog_offset_counter_y(0);
        }
    }

    public static void removeAllDialogs() {
        for (int i = 1; i <= lastChar; i++) {
            if (!charList[i].getDialog().isEmpty()) {
                charDialogSet(i, "", new RGBColor(1, 1, 1));
                charList[i].setDialog_life(0);
            }
        }
    }

    public static void charDialogSet(final int charIndex, final String charDialog, final RGBColor color) {
        if (charList[charIndex].isActive()) {
            charList[charIndex].setDialog(charDialog);
            charList[charIndex].setDialog_color(color);
            charList[charIndex].setDialog_life(10);
            charList[charIndex].setDialog_font_index(NORMAL_FONT);
            charList[charIndex].setDialog_offset_counter_y(0);
        }
    }

    public static void charDialogHitSet(final int charIndex, int charDamage) {
        String charDialog = "";

        if (charList[charIndex].isActive()) {
            charList[charIndex].setDialog_color(HIT_COLOR);
            charList[charIndex].setDialog_life(5);
            charList[charIndex].setDialog_font_index(HIT_FONT);
            charList[charIndex].setDialog_offset_counter_y(0);

            if (charDamage >= 150) charDialog = "¡" + charDamage + "!";
            else charDialog = String.valueOf(charDamage);

            charList[charIndex].setDialog(charDialog);
        }
    }

    public static void charDialogHitSet(final int charIndex, String charDialog) {
        if (charList[charIndex].isActive()) {
            charList[charIndex].setDialog_color(HIT_COLOR);
            charList[charIndex].setDialog_life(5);
            charList[charIndex].setDialog_font_index(HIT_FONT);
            charList[charIndex].setDialog_offset_counter_y(0);
            charList[charIndex].setDialog(charDialog);
        }
    }

    public static void removeDialogsNPCArea() {
        for (int x = charList[User.INSTANCE.getUserCharIndex()].getPos().getX() - HALF_WINDOW_TILE_WIDTH; x <= charList[User.INSTANCE.getUserCharIndex()].getPos().getX() + HALF_WINDOW_TILE_WIDTH; x++) {
            for (int y = charList[User.INSTANCE.getUserCharIndex()].getPos().getY() - HALF_WINDOW_TILE_HEIGHT; y <= charList[User.INSTANCE.getUserCharIndex()].getPos().getY() + HALF_WINDOW_TILE_HEIGHT; y++) {
                if (mapData[x][y].getCharIndex() > 0) {
                    if (charList[mapData[x][y].getCharIndex()].getName().length() <= 1)
                        removeDialog(mapData[x][y].getCharIndex());
                }
            }
        }
    }

    public static void renderDialogs(final Camera camera, final int x, final int y, final int pixelOffsetX, final int pixelOffsetY) {
        if (mapData[x][y].getCharIndex() != 0) {
            final Character chrActual = charList[mapData[x][y].getCharIndex()];

            if (!chrActual.getDialog().isEmpty()) {
                if (chrActual.getDialog_offset_counter_y() < 10)
                    chrActual.setDialog_offset_counter_y(chrActual.getDialog_offset_counter_y() + 50 * deltaTime);

                final int dX = (POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE) + ((int) (chrActual.getMoveOffsetX()) + pixelOffsetX);
                final int dY = (POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE) + ((int) (chrActual.getMoveOffsetY()) + pixelOffsetY);

                // Render dialog
                drawText(chrActual.getDialog(),
                        dX + 16 - getTextWidth(chrActual.getDialog(), true) / 2,
                        dY - 13 + chrActual.getBody().getHeadOffset().getY() - getTextHeight(chrActual.getDialog(), true) - (int) chrActual.getDialog_offset_counter_y(),
                        chrActual.getDialog_color(),
                        chrActual.getDialog_font_index(), true);
            }
        }
    }

    public static void updateDialogs() {
        // paso 1 seg?
        if (timerDialogs <= 0) {
            timerDialogs = 1.0f;
            for (int i = 1; i <= lastChar; i++) {
                if (!charList[i].getDialog().isEmpty()) {
                    charList[i].setDialog_life(charList[i].getDialog_life() - 1);
                    if (charList[i].getDialog_life() <= 0) {
                        charList[i].setDialog("");
                        charList[i].setDialog_color(new RGBColor(1, 1, 1));
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
