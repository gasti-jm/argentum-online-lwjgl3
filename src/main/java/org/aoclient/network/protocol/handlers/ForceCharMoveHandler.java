package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.game.models.Character.refreshAllChars;

public class ForceCharMoveHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(2)) return;
        buffer.readByte();

        Direction direction = Direction.values()[buffer.readByte() - 1];
        short userCharIndex = User.INSTANCE.getUserCharIndex();
        User.INSTANCE.moveCharbyHead(userCharIndex, direction);
        User.INSTANCE.moveScreen(direction);

        refreshAllChars();
    }

}
