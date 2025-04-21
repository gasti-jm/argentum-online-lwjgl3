package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class AreaChangedHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        int x = data.readByte();
        int y = data.readByte();

        User.get().areaChange(x, y);
    }
    
}
