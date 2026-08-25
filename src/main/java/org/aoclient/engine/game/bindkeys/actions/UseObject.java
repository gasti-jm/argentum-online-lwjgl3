package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.game.bindkeys.KeyAction;

public class UseObject implements KeyAction {
    @Override
    public void action() {
        Player.INSTANCE.getUserInventory().useItem();
    }
}
