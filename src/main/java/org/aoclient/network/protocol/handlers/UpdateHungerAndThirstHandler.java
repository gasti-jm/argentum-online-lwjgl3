package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class UpdateHungerAndThirstHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;

        // Remove packet ID
        data.readByte();

        User.get().setUserMaxAGU(data.readByte());
        User.get().setUserMinAGU(data.readByte());
        User.get().setUserMaxHAM(data.readByte());
        User.get().setUserMinHAM(data.readByte());
    }
}
