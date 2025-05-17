package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.utils.GameData.charList;

public class SetInvisibleHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(4)) return;

        // Remove packet ID
        data.readByte();
        charList[data.readInteger()].setInvisible(data.readBoolean());
        Logger.debug("handleSetInvisible Cargado!");
    }
}
