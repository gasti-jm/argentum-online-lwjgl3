package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UserCharIndexInServerHandler implements PacketHandler {
    
    private final Player user = Player.INSTANCE;

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
