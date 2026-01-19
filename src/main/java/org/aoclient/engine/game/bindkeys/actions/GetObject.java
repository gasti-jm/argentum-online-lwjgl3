package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import static org.aoclient.network.protocol.Protocol.pickUp;

public class GetObject implements KeyAction {
    @Override
    public void action() {
        pickUp();
    }
}
