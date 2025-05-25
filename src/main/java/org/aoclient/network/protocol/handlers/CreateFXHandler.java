package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class CreateFXHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(7)) return;
        buffer.readByte();

        short charIndex = buffer.readInteger();
        short fX = buffer.readInteger();
        short loops = buffer.readInteger();

        User.INSTANCE.setCharacterFx(charIndex, fX, loops);
    }

}
