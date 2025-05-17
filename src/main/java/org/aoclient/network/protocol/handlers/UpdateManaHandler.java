package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class UpdateManaHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        data.readByte();

        // variable global
        User.get().setUserMinMAN(data.readInteger());
    }
}
