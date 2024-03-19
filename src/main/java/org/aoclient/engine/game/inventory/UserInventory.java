package org.aoclient.engine.game.inventory;

import org.aoclient.engine.game.IntervalTimer;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.network.Protocol.writeEquipItem;
import static org.aoclient.network.Protocol.writeUseItem;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.renderer.FontText.drawText;
import static org.aoclient.engine.scenes.Camera.*;

/**
 * Clase creada unicamente para el inventario de nuestro usuario, viene heredada de la clase Inventory.
 */
public final class UserInventory extends Inventory {
    // Posicion original del picInv del frmMain original de AO
    private static final int MAIN_POS_X = 600;
    private static final int MAIN_POS_Y = 160;

    // Tamaño del inventario segun el frmMain original de AO
    private static final int MAIN_SIZE_WIDTH = 160;
    private static final int MAIN_SIZE_HEIGHT = 128;
    private static final int MAX_INVENTORY_SLOTS = 30;
    public static final int FLAGORO = MAX_INVENTORY_SLOTS + 1;

    // Intervalos del inventario.
    private static final float INT_USE_ITEM = 0.240f;
    private static final float INT_EQUIP_ITEM = 0.15f;

    private final RGBColor colorEquipped = new RGBColor(1.0f, 1.0f, 0.0f);
    private final IntervalTimer intervalUseItem = new IntervalTimer(INT_USE_ITEM);
    private final IntervalTimer intervalEquipItem = new IntervalTimer(INT_EQUIP_ITEM);


    /**
     * Inventory user constructor
     */
    public UserInventory(){
        super(MAIN_POS_X, MAIN_POS_Y, MAIN_SIZE_WIDTH, MAIN_SIZE_HEIGHT, MAX_INVENTORY_SLOTS);
    }

    /**
     * @desc: Actualiza y dibuja el inventario.
     */
    @Override
    public void drawInventory() {
        intervalUseItem.update();
        intervalEquipItem.update();

        if (slots.length == 0) return;

        // posiciones por slot
        int iX = posX;
        int iY = posY;

        // simulando un pictureBox
        drawRectangle(posX, posY, sWidth, sHeigth, null);

        for (int i = 0; i < slots.length; i++) {
            if (slots[i].grhIndex > 0) {
                drawGrhIndex(slots[i].grhIndex, iX, iY, null);
                drawText(String.valueOf(slots[i].amount), iX, iY + 20, null, 0, true, false, false);

                if (slots[i].equipped) {
                    drawText("E", iX + 23, iY, colorEquipped, 0, true, false, false);
                }
            }

            if (i == slotSelected) {
                drawGrhIndex(2, iX, iY, null);
            }

            // actualizamos la posicion en forma de tabla.
            iX += TILE_PIXEL_SIZE;
            if ((iX - posX) / TILE_PIXEL_SIZE == this.cantColumns) {
                iY += TILE_PIXEL_SIZE;
                iX = posX;
            }
        }

    }

    /**
     * @desc Realiza una accion al hacer doble click dentro del inventario.
     */
    public void dobleClickInventory() {
        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);

        // esta el mouse dentro del inventario??
        if (inInventoryArea()) {

            // Esta selecionado primero?
            if (x + (this.cantColumns * y) == this.slotSelected) {
                if (slots[slotSelected].grhIndex > 0) {
                    if (slots[slotSelected].objType.equippable) { // es equipable?
                        equipItem();
                    } else {
                        useItem();
                    }
                }
            }
        }
    }

    public void useItem() {
        if (slots[slotSelected].grhIndex > 0 && intervalUseItem.check()) {
            writeUseItem(this.slotSelected + 1);
        }
    }

    public void equipItem() {
        // no vamos a mandar un paquete al pedo.
        if (slots[slotSelected].grhIndex > 0 && slots[slotSelected].objType.equippable && intervalEquipItem.check()) {
            writeEquipItem(this.slotSelected + 1);
        }
    }

}
