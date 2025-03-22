package org.aoclient.engine.game.inventory;

import imgui.ImGui;

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
        super(FORM_POS_X, FORM_POS_Y, MAIN_SIZE_WIDTH, MAIN_SIZE_HEIGHT, MAX_INVENTORY_SLOTS);
    }

    @Override
    public void drawInventory() {
        if (slots.length == 0) return;

        // posiciones por slot
        int iX = posX;
        int iY = posY;

        for (Slot slot : slots) {
            if (slot.grhIndex > 0) {
                ImGui.setCursorPos(iX + 5, iY);

                ImGui.image(slot.objTexture.getId(), 32, 32);


                ImGui.setCursorPos(iX + 5, iY + 20);
                ImGui.text(String.valueOf(slot.amount));
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
