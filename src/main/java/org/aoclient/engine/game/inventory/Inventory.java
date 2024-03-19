package org.aoclient.engine.game.inventory;

import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.renderer.FontText.drawText;
import static org.aoclient.engine.scenes.Camera.*;

/**
 * Clase padre de inventario, ya que puede existir distintos inventarios (usuario, npc, etc).
 */
public class Inventory {
    protected int posX, posY;
    protected int sWidth, sHeigth;
    protected int cantRows, cantColumns;
    protected boolean visible;

    static class Slot {
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

    protected Slot[] slots;
    protected int slotSelected;

    public Inventory(int posX, int posY, int width, int heigth, int cantSlots){
        this.visible = true;
        this.slotSelected = 0;
        this.slots = new Slot[cantSlots];

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }

        this.posX = posX;
        this.posY = posY;

        this.sWidth = width;
        this.sHeigth = heigth;

        this.cantColumns = sWidth / TILE_PIXEL_SIZE;
        this.cantRows = sHeigth / TILE_PIXEL_SIZE;
    }

    /**
     * @desc: Chekeamos que el mouse este dentro del inventario.
     */
    public boolean inInventoryArea() {
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

        if (objType > 0)
            slots[slot].objType = E_ObjType.values()[objType - 1];

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

        drawRectangle(posX, posY, sWidth, sHeigth, null);
        for (int i = 0; i < slots.length; i++) {

            if (slots[i].grhIndex > 0) {
                drawGrhIndex(slots[i].grhIndex, iX,  iY, null);
                drawText(String.valueOf(slots[i].amount),  iX, iY + 20, null, 0, true, false, false);
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

    /**
     * @desc Selecciona un slot al hacer un solo click dentro del inventario.
     */
    public void clickInventory(){
        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);
        final int slot = x + (this.cantColumns * y);

        setSelectedSlot(slot); // actualizamos el slot seleccionado
    }

    /**
     * @return Getter del atributo slotSelected
     */
    public int getSlotSelected() {
        return slotSelected;
    }

    /**
     * @desc Setter del atributo slotSelected
     */
    private void setSelectedSlot(int slot) {
        slotSelected = slot;
    }

    public int getAmountSlotSelected() {
        return slots[slotSelected].amount;
    }

}
