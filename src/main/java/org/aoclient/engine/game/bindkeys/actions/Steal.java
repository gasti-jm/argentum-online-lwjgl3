package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.game.models.Skill;

import static org.aoclient.network.protocol.Protocol.work;

public class Steal implements KeyAction {
    @Override
    public void action() {
        work(Skill.THEFT.getId());
    }
}
