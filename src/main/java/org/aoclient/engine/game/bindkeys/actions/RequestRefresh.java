package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.game.intervals.Timers;

import static org.aoclient.network.protocol.Protocol.requestPositionUpdate;

public class RequestRefresh implements KeyAction {
    @Override
    public void action() {
        if (Timers.INT_SENTRPU.getTimer().check())
            requestPositionUpdate();
    }
}
