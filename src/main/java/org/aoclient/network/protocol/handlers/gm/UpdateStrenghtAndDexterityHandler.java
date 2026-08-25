package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class UpdateStrenghtAndDexterityHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();
        Player.INSTANCE.setUserStrg(buffer.readByte());
        Player.INSTANCE.setUserDext(buffer.readByte());
    }

}
