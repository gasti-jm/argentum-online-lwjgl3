package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class UpdateStrenghtAndDexterityHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        // Remove packet ID
        data.readByte();

        User.get().setUserStrg(data.readByte());
        User.get().setUserDext(data.readByte());
    }
    
}
