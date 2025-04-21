package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

import static org.aoclient.engine.utils.GameData.charList;

public class SetInvisibleHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(4)) return;

        // Remove packet ID
        data.readByte();
        charList[data.readInteger()].setInvisible(data.readBoolean());
        Logger.debug("handleSetInvisible Cargado!");
    }
}
