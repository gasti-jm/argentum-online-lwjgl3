package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class LevelUpHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        // Remove packet ID
        data.readByte();

        short skillPoints = (short) (User.get().getFreeSkillPoints() + data.readInteger());
        User.get().setFreeSkillPoints(skillPoints);
    }
    
}
