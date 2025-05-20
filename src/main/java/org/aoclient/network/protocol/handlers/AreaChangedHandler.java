package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class AreaChangedHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();
        int x = buffer.readByte();
        int y = buffer.readByte();
        User.INSTANCE.areaChange(x, y);
    }

}
