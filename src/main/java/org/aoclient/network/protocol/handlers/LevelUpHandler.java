package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class LevelUpHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        short skillPoints = (short) (User.INSTANCE.getFreeSkillPoints() + buffer.readInteger());
        User.INSTANCE.setFreeSkillPoints(skillPoints);
    }

}
