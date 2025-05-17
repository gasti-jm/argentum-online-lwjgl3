package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class UpdateGoldHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(5)) return;
        data.readByte();
        User.get().setUserGLD(data.readLong());
    }

}
