package org.aoclient.engine.game;

import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.connection.Protocol.writeEquipItem;
import static org.aoclient.connection.Protocol.writeUseItem;
import static org.aoclient.engine.renderer.Drawn.drawGrhIndex;
import static org.aoclient.engine.renderer.Drawn.drawText;
import static org.aoclient.engine.scenes.Camera.*;

public final class Inventory {
    // Constantes //////////////////////////////////////////////////

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
        E_ObjType objType;
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

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }

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
    public void setItem(int slot, short objIndex, int amount, boolean equipped, short grhIndex, short objType,
                        short maxHit, short minHit, short maxDef, short minDef, float value, String name) {


        slots[slot].amount      = amount;
        slots[slot].maxDef      = maxDef;
        slots[slot].minDef      = minDef;
        slots[slot].equipped    = equipped;
        slots[slot].grhIndex    = grhIndex;
        slots[slot].maxHit      = maxHit;
        slots[slot].minHit      = minHit;
        slots[slot].name        = name;
        slots[slot].objIndex    = objIndex;
        if (objType > 0) slots[slot].objType = E_ObjType.values()[objType - 1];
        slots[slot].value       = value;
    }

    /**
     * @desc: Dibujamos el inventario.
     */
    public void drawInventory() {
        if(slots.length == 0) return;

        // posiciones por slot
        int iX = posX;
        int iY = posY;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i].grhIndex > 0) {
                drawGrhIndex(slots[i].grhIndex, iX,  iY, null);

                drawText(String.valueOf(slots[i].amount),  iX, iY + 20, null, 0, false);

                if (slots[i].equipped) {
                    drawText("E",  iX + 20, iY, colorEquipped, 0, false);
                }
            }

            if(i == slotSelected) {
                drawGrhIndex(2, iX,  iY, null);
            }

            // actualizamos la posicion en forma de tabla.
            iX += TILE_PIXEL_SIZE;
            if((iX - posX) / TILE_PIXEL_SIZE == this.cantColumns) {
                iY += TILE_PIXEL_SIZE;
                iX = posX;
            }
        }
    }

    public void clickInventory(){
        if (inInventoryArea()) {
            final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
            final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);
            final int slot = x + (this.cantColumns * y);

            setSelectedSlot(slot); // actualizamos el slot seleccionado
        }
    }

    public void dobleClickInventory() {
        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);

        // esta el mouse dentro del inventario??
        if (inInventoryArea()) {

            // Esta selecionado primero?
            if (x + (this.cantColumns * y) == this.slotSelected){

                // no vamos a mandar un paquete al pedo.
                if (slots[slotSelected].grhIndex > 0) {
                    if (slots[slotSelected].objType.equippable) {
                        writeEquipItem(this.slotSelected + 1);
                    } else {
                        writeUseItem(this.slotSelected + 1);
                    }
                }

            }
        }
    }

    private void setSelectedSlot(int slot) {
        slotSelected = slot;
    }

}
