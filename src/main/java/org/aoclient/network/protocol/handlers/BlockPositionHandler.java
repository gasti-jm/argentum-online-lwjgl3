package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.mapData;

public class BlockPositionHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(4)) return;
        buffer.readByte();

        int x = buffer.readByte();
        int y = buffer.readByte();

        mapData[x][y].setBlocked(buffer.readBoolean());
    }

}
