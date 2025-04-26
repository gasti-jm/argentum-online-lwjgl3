package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class CreateFXHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(7)) return;

        // Remove packet ID
        data.readByte();

        short charIndex = data.readInteger();
        short fX = data.readInteger();
        short loops = data.readInteger();

        User.get().setCharacterFx(charIndex, fX, loops);
    }
    
}
