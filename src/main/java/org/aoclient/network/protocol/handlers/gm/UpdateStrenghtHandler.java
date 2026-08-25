package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class UpdateStrenghtHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(2)) return;
        buffer.readByte();
        Player.INSTANCE.setUserStrg(buffer.readByte());
    }

}
