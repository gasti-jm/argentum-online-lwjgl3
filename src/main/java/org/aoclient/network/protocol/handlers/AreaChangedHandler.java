package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class AreaChangedHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;
        data.readByte();
        int x = data.readByte();
        int y = data.readByte();
        User.INSTANCE.areaChange(x, y);
    }

}
