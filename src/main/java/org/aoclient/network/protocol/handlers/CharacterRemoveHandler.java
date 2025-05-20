package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.game.models.Character.eraseChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;

public class CharacterRemoveHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        eraseChar(buffer.readInteger());
        refreshAllChars();
    }

}
