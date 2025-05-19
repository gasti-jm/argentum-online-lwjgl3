package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class UpdateHungerAndThirstHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(5)) return;

        // Remove packet ID
        data.readByte();

        User.INSTANCE.setUserMaxAGU(data.readByte());
        User.INSTANCE.setUserMinAGU(data.readByte());
        User.INSTANCE.setUserMaxHAM(data.readByte());
        User.INSTANCE.setUserMinHAM(data.readByte());
    }
}
