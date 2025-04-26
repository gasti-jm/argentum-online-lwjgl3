package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

import static org.aoclient.engine.utils.GameData.charList;

public class UserCharIndexInServerHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        User.get().setUserCharIndex(data.readInteger());
        User.get().getUserPos().setX(charList[User.get().getUserCharIndex()].getPos().getX());
        User.get().getUserPos().setY(charList[User.get().getUserCharIndex()].getPos().getY());
        User.get().setUnderCeiling(User.get().checkUnderCeiling());
    }

}
