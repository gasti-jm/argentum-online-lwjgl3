package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class LoggedHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        User.INSTANCE.setUserConected(true);
    }

}
