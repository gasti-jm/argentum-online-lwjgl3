package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.utils.Log;
import org.tinylog.Logger;

import static org.aoclient.network.protocol.Protocol.resucitationToggle;

public class ToggleResuscitationSafe implements KeyAction {
    @Override
    public void action() {
        resucitationToggle();
    }
}
