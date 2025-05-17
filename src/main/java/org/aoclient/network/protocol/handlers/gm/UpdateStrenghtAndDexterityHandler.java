package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class UpdateStrenghtAndDexterityHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        // Remove packet ID
        data.readByte();

        User.get().setUserStrg(data.readByte());
        User.get().setUserDext(data.readByte());
    }
    
}
