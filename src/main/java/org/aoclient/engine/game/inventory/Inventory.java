package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;

/**
 * Clase padre de inventario, ya que puede existir distintos inventarios (usuario, npc, etc).
 */

public abstract class Inventory {

    public static final int MAX_INVENTORY_SLOTS = 30;
    public static final int MAX_NPC_INVENTORY_SLOTS = 50;

    protected static final int selectedColor = ImGui.getColorU32(0f, 1f, 0f, 1f);

    protected int posX, posY;
    protected int sWidth, sHeigth;
    protected int cantRows, cantColumns;
    protected boolean visible;

    protected RGBColor amountColor = new RGBColor(1, 1, 1);
    protected Slot[] slots;
    protected int slotSelected;
    public Inventory(int posX, int posY, int width, int heigth, int cantSlots) {
        this.visible = true;
        this.slotSelected = 0;
        this.slots = new Slot[cantSlots];

        for (int i = 0; i < slots.length; i++)
            slots[i] = new Slot();

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
        if (MouseListener.getX() < posX || MouseListener.getX() > posX + sWidth) return false;
        if (MouseListener.getY() < posY || MouseListener.getY() > posY + sHeigth) return false;
        return true;
    }

    /**
     * @desc: Agregamos un item al slot del inventario.
     */
    public void setItem(int slot, short objIndex, int amount, boolean equipped, short grhIndex, int objType,
                        short maxHit, short minHit, short maxDef, short minDef, float value, String name) {

        slots[slot].amount = amount;
        slots[slot].maxDef = maxDef;
        slots[slot].minDef = minDef;
        slots[slot].equipped = equipped;
        slots[slot].grhIndex = grhIndex;

        if (grhIndex > 0) slots[slot].objTexture = Surface.get().getTexture(grhData[grhIndex].getFileNum());

        slots[slot].maxHit = maxHit;
        slots[slot].minHit = minHit;
        slots[slot].name = name;
        slots[slot].objIndex = objIndex;

        if (objType > 0) slots[slot].objType = E_ObjType.values()[objType - 1];

        slots[slot].value = value;
    }

    /**
     * @desc: Dibujamos el inventario.
     */
    public abstract void drawInventory();

    /**
     * @desc Selecciona un slot al hacer un solo click dentro del inventario.
     */
    public void clickInventory() {
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

        Texture objTexture;

    }

}
