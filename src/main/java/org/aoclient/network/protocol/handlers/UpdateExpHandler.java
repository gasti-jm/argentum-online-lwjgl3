package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class UpdateExpHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;
        data.readByte();
        // Get data and update
        User.get().setUserExp(data.readLong());
    }
}
