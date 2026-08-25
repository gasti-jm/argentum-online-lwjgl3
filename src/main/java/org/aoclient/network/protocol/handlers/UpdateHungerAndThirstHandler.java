package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

public class UpdateHungerAndThirstHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        buffer.readByte();

        Player.INSTANCE.setUserMaxAGU(buffer.readByte());
        Player.INSTANCE.setUserMinAGU(buffer.readByte());
        Player.INSTANCE.setUserMaxHAM(buffer.readByte());
        Player.INSTANCE.setUserMinHAM(buffer.readByte());
    }

}
