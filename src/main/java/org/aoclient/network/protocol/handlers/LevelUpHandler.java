package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class LevelUpHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        // Remove packet ID
        data.readByte();

        short skillPoints = (short) (User.get().getFreeSkillPoints() + data.readInteger());
        User.get().setFreeSkillPoints(skillPoints);
    }
    
}
