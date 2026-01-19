package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;

import static org.aoclient.engine.scenes.GameScene.autoMove;

public class AutoMove implements KeyAction {
    @Override
    public void action() {
        autoMove = !autoMove;
    }
}
