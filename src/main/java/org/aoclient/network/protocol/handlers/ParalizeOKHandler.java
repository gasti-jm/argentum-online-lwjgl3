package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

import static org.aoclient.engine.utils.GameData.charList;

public class ParalizeOKHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        charList[User.get().getUserCharIndex()].setParalizado(!charList[User.get().getUserCharIndex()].isParalizado());
    }

}
