package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;

import static org.aoclient.network.protocol.Protocol.meditate;

public class Meditate implements KeyAction {
    @Override
    public void action() {
        meditate();
    }
}
