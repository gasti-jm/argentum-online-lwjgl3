package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import org.aoclient.engine.game.models.ObjectType;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;

/**
 * Clase abstracta que sirve como base para los diferentes sistemas de inventario.
 * <p>
 * Esta clase implementa la funcionalidad comun para almacenar, gestionar y mostrar los items que un personaje o NPC puede
 * poseer.
 * <p>
 * Define la estructura fundamental de todo inventario mediante:
 * <ul>
 * <li>Una matriz de slots donde se almacenan los objetos
 * <li>Control de posicionamiento y dimensiones en pantalla
 * <li>Manejo de interacciones con el mouse
 * <li>Sistema de seleccion de slots
 * </ul>
 */

public abstract class Inventory {

    public static final int MAX_INVENTORY_SLOTS = 30;
    public static final int MAX_NPC_INVENTORY_SLOTS = 50;

    protected static final int selectedColor = ImGui.getColorU32(1f, 0f, 0f, 1f);

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
     *  Chekeamos que el mouse este dentro del inventario.
     */
    public boolean inInventoryArea() {
        if (MouseListener.getX() < posX || MouseListener.getX() > posX + sWidth) return false;
        if (MouseListener.getY() < posY || MouseListener.getY() > posY + sHeigth) return false;
        return true;
    }

    /**
     *  Chekeamos que el mouse este dentro del inventario.
     */
    public boolean inInventoryArea(float mouseX, float mouseY) {
        if (mouseX < posX || mouseX > posX + sWidth) return false;
        if (mouseY < posY || mouseY > posY + sHeigth) return false;
        return true;
    }

    /**
     *  Agregamos un item al slot del inventario.
     */
    public void setItem(int slot, short objIndex, int amount, boolean equipped, short grhIndex, int objType,
                        short maxHit, short minHit, short maxDef, short minDef, float value, String name) {

        slots[slot].amount = amount;
        slots[slot].maxDef = maxDef;
        slots[slot].minDef = minDef;
        slots[slot].equipped = equipped;
        slots[slot].grhIndex = grhIndex;

        if (grhIndex > 0) slots[slot].objTexture = Surface.INSTANCE.getTexture(grhData[grhIndex].getFileNum());

        slots[slot].maxHit = maxHit;
        slots[slot].minHit = minHit;
        slots[slot].name = name;
        slots[slot].objIndex = objIndex;

        if (objType > 0) slots[slot].objType = ObjectType.values()[objType - 1];

        slots[slot].value = value;
    }

    /**
     *  Dibujamos el inventario.
     */
    public abstract void drawInventory();

    /**
     *  Selecciona un slot al hacer un solo click dentro del inventario.
     */
    public void clickInventory() {
        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);
        final int slot = x + (this.cantColumns * y);
        setSelectedSlot(slot); // actualizamos el slot seleccionado
    }

    /**
     *  Selecciona un slot al hacer un solo click dentro del inventario (Sirve para los frms de ImGUI).
     */
    public void clickInventory(float mouseX, float mouseY) {
        final int x = (int) ((mouseX - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((mouseY - posY) / TILE_PIXEL_SIZE);
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
     *  Setter del atributo slotSelected
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
        ObjectType objType;
        short maxDef;
        short minDef;
        short maxHit;
        short minHit;

        Texture objTexture;

    }

    public float getValue(int slot) {
        return slots[slot].value;
    }

    public String getItemName(int slot) {
        return slots[slot].name;
    }

    public short getMaxHit(int slot) {
        return slots[slot].maxHit;
    }

    public short getMinHit(int slot) {
        return slots[slot].maxHit;
    }

    public short getMaxDef(int slot) {
        return slots[slot].maxDef;
    }

    public short getMinDef(int slot) {
        return slots[slot].minDef;
    }

    public ObjectType getObjType(int slot) {
        return slots[slot].objType;
    }

}
