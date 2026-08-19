package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.utils.Log;
import org.tinylog.Logger;

import static org.aoclient.network.protocol.Protocol.safeToggle;

public class ToggleSafeMode implements KeyAction {
    @Override
    public void action() {
        safeToggle();
    }
}
