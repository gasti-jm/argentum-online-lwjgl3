package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

public class UpdateGoldHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        buffer.readByte();
        Player.INSTANCE.setUserGLD(buffer.readLong());
    }

}
