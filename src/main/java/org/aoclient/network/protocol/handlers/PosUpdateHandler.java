package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.GameData.mapData;

public class PosUpdateHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        // Remove packet ID
        data.readByte();

        int x = User.get().getUserPos().getX();
        int y = User.get().getUserPos().getY();
        int userCharIndex = User.get().getUserCharIndex();

        // Remove char form old position
        if (mapData[x][y].getCharIndex() == userCharIndex) {
            mapData[x][y].setCharIndex(0);
        }

        // Set new pos
        User.get().getUserPos().setX(data.readByte());
        User.get().getUserPos().setY(data.readByte());

        // again xd
        x = User.get().getUserPos().getX();
        y = User.get().getUserPos().getY();

        mapData[x][y].setCharIndex(userCharIndex);
        charList[userCharIndex].getPos().setX(x);
        charList[userCharIndex].getPos().setY(y);
    }
}
