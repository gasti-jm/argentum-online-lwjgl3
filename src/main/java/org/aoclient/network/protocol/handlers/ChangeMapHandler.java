package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.utils.GameData;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.bLluvia;

public class ChangeMapHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        buffer.readByte();

        short userMap = buffer.readInteger();
        Player.INSTANCE.setUserMap(userMap);

        // Once on-the-fly editor is implemented check for map version before loading....
        // For now we just drop it
        buffer.readInteger();

        GameData.loadMap(userMap);

        if (!bLluvia[userMap] && Rain.INSTANCE.isRaining()) Rain.INSTANCE.stopRainingSoundLoop();

    }

}
