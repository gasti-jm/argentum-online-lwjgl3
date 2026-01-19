package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.bindkeys.KeyAction;

public class EquipObject implements KeyAction {
    @Override
    public void action() {
        User.INSTANCE.getUserInventory().equipItem();
    }
}
