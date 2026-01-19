package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;

import static org.aoclient.engine.scenes.GameScene.intervalToUpdatePos;
import static org.aoclient.network.protocol.Protocol.requestPositionUpdate;

public class RequestRefresh implements KeyAction {
    @Override
    public void action() {
        if (intervalToUpdatePos.check())
            requestPositionUpdate();
    }
}
