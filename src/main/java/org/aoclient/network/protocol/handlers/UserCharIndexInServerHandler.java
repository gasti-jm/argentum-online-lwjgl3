package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UserCharIndexInServerHandler implements PacketHandler {
    
    private final User user = User.INSTANCE;

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        user.setUserCharIndex(buffer.readInteger());
        user.getUserPos().setX(charList[user.getUserCharIndex()].getPos().getX());
        user.getUserPos().setY(charList[user.getUserCharIndex()].getPos().getY());
        user.setUnderCeiling(user.checkUnderCeiling());
    }

}
