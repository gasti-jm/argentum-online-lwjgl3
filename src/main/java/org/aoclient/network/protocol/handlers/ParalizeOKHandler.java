package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class ParalizeOKHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        charList[Player.INSTANCE.getUserCharIndex()].setParalizado(!charList[Player.INSTANCE.getUserCharIndex()].isParalizado());
    }

}
