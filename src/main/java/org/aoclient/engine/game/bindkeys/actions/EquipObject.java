package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.game.bindkeys.KeyAction;

public class EquipObject implements KeyAction {
    @Override
    public void action() {
        Player.INSTANCE.getUserInventory().equipItem();
    }
}
