package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import org.tinylog.Logger;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;

/**
 * <p>
 * Proporciona la implementacion especifica para manejar y mostrar los objetos que un NPC tiene en su inventario, permitiendo al
 * jugador ver e interactuar con estos elementos durante el comercio u otras interacciones con NPCs.
 */

public class NPCInventory extends Inventory {

    // Posicion original del picInv del frmComerciar original de AO
    private static final int FORM_POS_X = 40;
    private static final int FORM_POS_Y = 131;

    // Tamaño del inventario segun el frmComerciar original de AO
    private static final int MAIN_SIZE_WIDTH = 160;
    private static final int MAIN_SIZE_HEIGHT = 256;

    public NPCInventory() {
        super(FORM_POS_X, FORM_POS_Y, MAIN_SIZE_WIDTH, MAIN_SIZE_HEIGHT, MAX_NPC_INVENTORY_SLOTS);
    }

    @Override
    public void drawInventory() {
        if (slots.length == 0) return;

        // posiciones por slot
        int iX = posX;
        int iY = posY;

        final float wposX = ImGui.getWindowPosX() + 5;
        final float wposY = ImGui.getWindowPosY() + 1;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i].grhIndex > 0) {
                ImGui.setCursorPos(iX + 5, iY);

                if (i == slotSelected) {
                    ImGui.getWindowDrawList().addLine(wposX + iX + 31, wposY + iY,
                            wposX + iX + 31, wposY + iY + 31, selectedColor);

                    ImGui.getWindowDrawList().addLine(wposX + iX, wposY + iY + 31,
                            wposX+ iX + 31, wposY + iY + 31, selectedColor);
                }

                ImGui.image(slots[i].objTexture.getId(), 32, 32);


                ImGui.setCursorPos(iX + 5, iY + 20);
                ImGui.text(String.valueOf(slots[i].amount));
            }

            // actualizamos la posicion en forma de tabla.
            iX += TILE_PIXEL_SIZE;
            if ((iX - posX) / TILE_PIXEL_SIZE == this.cantColumns) {
                iY += TILE_PIXEL_SIZE;
                iX = posX;
            }
        }

    }

}
