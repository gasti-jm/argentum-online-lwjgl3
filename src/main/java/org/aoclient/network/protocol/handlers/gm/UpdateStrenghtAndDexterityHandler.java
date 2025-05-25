package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class UpdateStrenghtAndDexterityHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();
        User.INSTANCE.setUserStrg(buffer.readByte());
        User.INSTANCE.setUserDext(buffer.readByte());
    }

}
