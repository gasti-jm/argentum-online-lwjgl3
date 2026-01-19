package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;

import static org.aoclient.network.protocol.Protocol.attack;

public class Attack implements KeyAction {
    @Override
    public void action() {
        attack();
    }
}
