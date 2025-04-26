package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class UpdateGoldHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;

        // Remove packet ID
        data.readByte();

        User.get().setUserGLD(data.readLong());
    }
}
