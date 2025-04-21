package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

import static org.aoclient.engine.game.models.Character.eraseChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;

public class CharacterRemoveHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        eraseChar(data.readInteger());
        refreshAllChars();
    }
}
