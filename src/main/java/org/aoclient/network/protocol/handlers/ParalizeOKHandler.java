package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class ParalizeOKHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        charList[User.INSTANCE.getUserCharIndex()].setParalizado(!charList[User.INSTANCE.getUserCharIndex()].isParalizado());
    }

}
