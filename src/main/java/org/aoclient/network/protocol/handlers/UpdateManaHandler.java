package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class UpdateManaHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        // variable global
        User.get().setUserMinMAN(data.readInteger());
    }
}
