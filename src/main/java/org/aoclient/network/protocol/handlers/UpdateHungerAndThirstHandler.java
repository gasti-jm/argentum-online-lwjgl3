package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class UpdateHungerAndThirstHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        buffer.readByte();

        User.INSTANCE.setUserMaxAGU(buffer.readByte());
        User.INSTANCE.setUserMinAGU(buffer.readByte());
        User.INSTANCE.setUserMaxHAM(buffer.readByte());
        User.INSTANCE.setUserMinHAM(buffer.readByte());
    }

}
