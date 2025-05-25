package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.GameData.mapData;

public class PosUpdateHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        int x = User.INSTANCE.getUserPos().getX();
        int y = User.INSTANCE.getUserPos().getY();
        int userCharIndex = User.INSTANCE.getUserCharIndex();

        // Remove char form old position
        if (mapData[x][y].getCharIndex() == userCharIndex) mapData[x][y].setCharIndex(0);

        // Set new pos
        User.INSTANCE.getUserPos().setX(buffer.readByte());
        User.INSTANCE.getUserPos().setY(buffer.readByte());

        // again xd
        x = User.INSTANCE.getUserPos().getX();
        y = User.INSTANCE.getUserPos().getY();

        mapData[x][y].setCharIndex(userCharIndex);
        charList[userCharIndex].getPos().setX(x);
        charList[userCharIndex].getPos().setY(y);
    }

}
