package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UserCharIndexInServerHandler implements PacketHandler {
    
    private final User user = User.INSTANCE;

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        data.readByte();

        user.setUserCharIndex(data.readInteger());
        user.getUserPos().setX(charList[user.getUserCharIndex()].getPos().getX());
        user.getUserPos().setY(charList[user.getUserCharIndex()].getPos().getY());
        user.setUnderCeiling(user.checkUnderCeiling());
    }

}
