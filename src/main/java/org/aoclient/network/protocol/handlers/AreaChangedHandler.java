package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

public class AreaChangedHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();
        int x = buffer.readByte();
        int y = buffer.readByte();
        Player.INSTANCE.areaChange(x, y);
    }

}
