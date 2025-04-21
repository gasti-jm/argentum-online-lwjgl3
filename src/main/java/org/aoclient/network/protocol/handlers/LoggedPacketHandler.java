package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class LoggedPacketHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        User.get().setUserConected(true);
    }

}
