package org.aoclient.engine.game;

import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.connection.Protocol.writeUseItem;
import static org.aoclient.engine.renderer.Drawn.drawGrhIndex;
import static org.aoclient.engine.renderer.Drawn.drawText;
import static org.aoclient.engine.scenes.Camera.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public final class Inventory {
    // Constantes ///////////////////////////////////////////////////

    // posicion del inventario en pantalla
    public static final int MAIN_POS_X = 600;
    public static final int MAIN_POS_Y = 160;

    // size
    public static final int MAIN_SIZE_WIDTH = 160;
    public static final int MAIN_SIZE_HEIGHT = 128;
    public static final int MAX_INVENTORY_SLOTS = 30;

    ////////////////////////////////////////////////////////////////

    private int posX, posY;
    private int sWidth, sHeigth;
    private int cantRows, cantColumns;

    private boolean visible;

    class Slot {
        short objIndex;
        String name;
        short grhIndex;
        int amount;
        boolean equipped;
        float value;
        short objType;
        short maxDef;
        short minDef;
        short maxHit;
        short minHit;
    }

    private Slot slots[];
    private int slotSelected;

    private RGBColor colorEquipped = new RGBColor(1.0f, 1.0f, 0.0f);

    /**
     * Constructor para inventario de usuario
     */
    public Inventory(){
        this.visible = true;
        this.slotSelected = 0;
        this.slots = new Slot[MAX_INVENTORY_SLOTS];

        this.posX = MAIN_POS_X;
        this.posY = MAIN_POS_Y;

        this.sWidth = MAIN_SIZE_WIDTH;
        this.sHeigth = MAIN_SIZE_HEIGHT;

        this.cantColumns = sWidth / TILE_PIXEL_SIZE;
        this.cantRows = sHeigth / TILE_PIXEL_SIZE;
    }

    /**
     * @desc: Chekeamos que el mouse este dentro del inventario.
     */
    private boolean inInventoryArea() {
        if (MouseListener.getX() < posX || MouseListener.getX() > posX + sWidth)
            return false;

        if (MouseListener.getY() < posY || MouseListener.getY() > posY + sHeigth)
            return false;

        return true;
    }

    /**
     * @desc: Agregamos un item al slot del inventario.
     */
    public void setItem(byte slot, short objIndex, int amount, boolean equipped, short grhIndex, short objType,
                        short maxHit, short minHit, short maxDef, short minDef, float value, String name) {

        slots[slot] = new Slot();
        slots[slot].amount      = amount;
        slots[slot].maxDef      = maxDef;
        slots[slot].minDef      = minDef;
        slots[slot].equipped    = equipped;
        slots[slot].grhIndex    = grhIndex;
        slots[slot].maxHit      = maxHit;
        slots[slot].minHit      = minHit;
        slots[slot].name        = name;
        slots[slot].objIndex    = objIndex;
        slots[slot].objType     = objType;
        slots[slot].value       = value;
    }

    /**
     * @desc: Dibujamos el inventario.
     */
    public void drawInventory() {
        if(slots.length == 0) return;

        // posiciones por slot
        int iX = 0;
        int iY = 0;

        for (Slot slot: slots) {
            if (slot == null) break;

            if (slot.grhIndex > 0) {

                // primero actualizamos las columnas

                drawGrhIndex(slot.grhIndex, posX + iX, posY + iY, null);

                if (slot.equals(slots[slotSelected])) {
                    drawGrhIndex(2, posX + iX, posY + iY, null);
                }

                drawGrhIndex(slot.grhIndex, posX + iX, posY + iY, null);


                drawText(String.valueOf(slot.amount), posX + iX, posY + iY + 20, null, 0, false);

                if(slot.equipped) {
                    drawText("E", posX + iX + 20, posY + iY, colorEquipped, 0, false);
                }

                // actualizamos la posicion en forma de grilla.
                iX += TILE_PIXEL_SIZE;
                if(iX / TILE_PIXEL_SIZE == this.cantColumns) {
                    iY += TILE_PIXEL_SIZE;
                    iX = 0;
                }

            }
        }
    }

    public void clickInventory(){
        if (inInventoryArea()) {
            // actualizamos el slot seleccionado
            setSelectedSlot((int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE),
                    (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE));
        }
    }

    public void dobleClickInventory() {
        if (inInventoryArea()) {
            writeUseItem(this.slotSelected + 1);
        }
    }


    private void setSelectedSlot(int x, int y) {
        slotSelected = x + (this.cantColumns * y);
    }

}
