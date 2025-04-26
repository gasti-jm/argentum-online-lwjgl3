package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class UpdateStrenghtHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2)) return;
        data.readByte();
        User.get().setUserStrg(data.readByte());
    }

}
