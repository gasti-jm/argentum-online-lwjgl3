package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.GameData.mapData;

public class PosUpdateHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        int x = Player.INSTANCE.getUserPos().getX();
        int y = Player.INSTANCE.getUserPos().getY();
        int userCharIndex = Player.INSTANCE.getUserCharIndex();

        // Remove char form old position
        if (mapData[x][y].getCharIndex() == userCharIndex) mapData[x][y].setCharIndex(0);

        // Set new pos
        Player.INSTANCE.getUserPos().setX(buffer.readByte());
        Player.INSTANCE.getUserPos().setY(buffer.readByte());

        // again xd
        x = Player.INSTANCE.getUserPos().getX();
        y = Player.INSTANCE.getUserPos().getY();

        mapData[x][y].setCharIndex(userCharIndex);
        charList[userCharIndex].getPos().setX(x);
        charList[userCharIndex].getPos().setY(y);
    }

}
