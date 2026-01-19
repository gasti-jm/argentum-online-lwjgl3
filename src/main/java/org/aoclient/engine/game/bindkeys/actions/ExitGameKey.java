package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;

import static org.aoclient.network.protocol.Protocol.quit;

public class ExitGameKey implements KeyAction {
    @Override
    public void action() {
        quit();
    }
}
