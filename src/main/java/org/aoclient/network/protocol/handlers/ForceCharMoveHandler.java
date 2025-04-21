package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

import static org.aoclient.engine.game.models.Character.refreshAllChars;

public class ForceCharMoveHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2)) return;

        data.readByte();

        E_Heading direction = E_Heading.values()[data.readByte() - 1];
        short userCharIndex = User.get().getUserCharIndex();
        User.get().moveCharbyHead(userCharIndex, direction);
        User.get().moveScreen(direction);

        refreshAllChars();
    }

}
