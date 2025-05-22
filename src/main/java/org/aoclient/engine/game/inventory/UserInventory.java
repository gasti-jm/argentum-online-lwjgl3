package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import org.aoclient.engine.game.IntervalTimer;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.network.protocol.Protocol.writeEquipItem;
import static org.aoclient.network.protocol.Protocol.writeUseItem;

/**
 * <p>
 * Esta clase proporciona la implementacion especifica para el manejo del inventario del personaje controlado por el jugador,
 * incluyendo la visualizacion de los objetos, interaccion mediante clicks y funcionalidades para usar y equipar items.
 * <p>
 * El inventario del usuario mantiene temporizadores para controlar la frecuencia con la que se pueden realizar acciones como usar
 * o equipar objetos, evitando el uso excesivamente rapido de estas funcionalidades.
 * <p>
 * A diferencia del inventario de NPCs, el inventario del usuario permite interacciones completas como la seleccion de objetos,
 * uso de doble click para utilizar o equipar items, y muestra informacion visual sobre que objetos estan equipados actualmente
 * mediante indicadores especiales en la interfaz.
 */

public final class UserInventory extends Inventory implements Cloneable {

    public static final int FLAGORO = MAX_INVENTORY_SLOTS + 1;
    // Posicion del picInv del frmMain original de AO
    private static final int MAIN_POS_X = 600;
    private static final int MAIN_POS_Y = 160;

    // posicion del picInv para el frmComerce
    public static final int COMERCE_POS_X = 263;
    public static final int COMERCE_POS_Y = 131;

    public static final int BANK_POS_X = 268;
    public static final int BANK_POS_Y = 160;

    // Tamaño del inventario segun el frmMain original de AO
    private static final int MAIN_SIZE_WIDTH = 160;
    private static final int MAIN_SIZE_HEIGHT = 128;

    // Intervalos del inventario.
    private static final float INT_USE_ITEM = 0.240f;
    private static final float INT_EQUIP_ITEM = 0.15f;
    private final IntervalTimer intervalUseItem = new IntervalTimer(INT_USE_ITEM);
    private final IntervalTimer intervalEquipItem = new IntervalTimer(INT_EQUIP_ITEM);

    private boolean visible;
    private boolean invComerce;

    /**
     * Inventory user constructor
     */
    public UserInventory() {
        super(MAIN_POS_X, MAIN_POS_Y, MAIN_SIZE_WIDTH, MAIN_SIZE_HEIGHT, MAX_INVENTORY_SLOTS);
        this.visible = true;
        this.invComerce = false;
    }

    public void updateTimers() {
        intervalUseItem.update();
        intervalEquipItem.update();
    }

    /**
     * @desc: Actualiza y dibuja el inventario.
     */
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
                ImGui.image(slots[i].objTexture.getId(), 32, 32);

                if(!invComerce) {
                    if (ImGui.isItemHovered()) ImGui.setTooltip(slots[i].name);
                }

                if (invComerce) {
                    if (i == slotSelected) {
                        ImGui.getWindowDrawList().addLine(wposX + iX + 31, wposY + iY,
                                wposX + iX + 31, wposY + iY + 31, selectedColor);

                        ImGui.getWindowDrawList().addLine(wposX + iX, wposY + iY + 31,
                                wposX+ iX + 31, wposY + iY + 31, selectedColor);
                    }
                } else {
                    if (i == slotSelected) {
                        ImGui.getWindowDrawList().addLine(iX + 31, iY, iX + 31, iY + 31, selectedColor);
                        ImGui.getWindowDrawList().addLine(iX, iY + 31, iX + 31, iY + 31, selectedColor);
                    }
                }


                ImGui.setCursorPos(iX + 5, iY + 20);
                ImGui.text(String.valueOf(slots[i].amount));

                if(!invComerce) {
                    if (slots[i].equipped) {
                        ImGui.setCursorPos(iX + 28, iY);
                        ImGui.textColored(ImGui.getColorU32(1f, 1f, 0f, 1f), "E");
                    }
                }
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
        if (!visible) return;

        final int x = (int) ((MouseListener.getX() - posX) / TILE_PIXEL_SIZE);
        final int y = (int) ((MouseListener.getY() - posY) / TILE_PIXEL_SIZE);

        // esta el mouse dentro del inventario??
        if (inInventoryArea()) {
            // Esta selecionado primero?
            if (x + (this.cantColumns * y) == this.slotSelected) {
                if (slots[slotSelected].grhIndex > 0) {
                    // es equipable?
                    if (slots[slotSelected].objType.isEquippable()) equipItem();
                    else useItem();
                }
            }
        }
    }

    public void useItem() {
        if (slots[slotSelected].grhIndex > 0 && intervalUseItem.check()) writeUseItem(this.slotSelected + 1);
    }

    public void equipItem() {
        // no vamos a mandar un paquete al pedo.
        if (slots[slotSelected].grhIndex > 0 && slots[slotSelected].objType.isEquippable() && intervalEquipItem.check())
            writeEquipItem(this.slotSelected + 1);

    }

    public void setVisible(boolean value) {
        this.visible = value;
    }

    public void transformInvComerce(boolean bank) {
        if (bank) {
            this.posX = BANK_POS_X;
            this.posY = BANK_POS_Y;
        } else {
            this.posX = COMERCE_POS_X;
            this.posY = COMERCE_POS_Y;
        }

        this.invComerce = true;
    }

    @Override
    public UserInventory clone() {
        try {
            return (UserInventory) super.clone(); // copia superficial
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
