package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

public class CommerceEndHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        Player.INSTANCE.setUserComerciando(false);
    }

}
