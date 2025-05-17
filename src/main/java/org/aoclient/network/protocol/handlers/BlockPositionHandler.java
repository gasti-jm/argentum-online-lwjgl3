package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.mapData;

public class BlockPositionHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(4)) return;

        data.readByte();

        int x = data.readByte();
        int y = data.readByte();

        mapData[x][y].setBlocked(data.readBoolean());
    }
    
}
