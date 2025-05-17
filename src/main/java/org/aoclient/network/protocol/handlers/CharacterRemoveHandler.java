package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.game.models.Character.eraseChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;

public class CharacterRemoveHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        data.readByte();

        eraseChar(data.readInteger());
        refreshAllChars();
    }
}
