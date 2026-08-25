package org.aoclient.engine.game.player;

import org.aoclient.engine.game.inventory.InventorySpells;
import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.game.models.Position;

public final class UserInfo {
    String name;

    // pos
    short map;
    Position userPos;
    Position addToUserPos;

    // area limits
    int minLimiteX, maxLimiteX;
    int minLimiteY, maxLimiteY;

    // inventories
    final UserInventory userInventory;
    final InventorySpells inventorySpells;

    UserInfo() {
        this.userPos = new Position();
        this.addToUserPos = new Position();
        this.userInventory = new UserInventory();
        this.inventorySpells = new InventorySpells();
    }
}