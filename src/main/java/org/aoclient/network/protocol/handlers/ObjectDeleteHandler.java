package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;

import static org.aoclient.engine.utils.GameData.mapData;

public class ObjectDeleteHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        int x = data.readByte();
        int y = data.readByte();

        mapData[x][y].getObjGrh().setGrhIndex((short) 0);
    }
    
}
