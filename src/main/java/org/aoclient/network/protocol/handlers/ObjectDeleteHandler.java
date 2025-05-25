package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.mapData;

public class ObjectDeleteHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        int x = buffer.readByte();
        int y = buffer.readByte();

        mapData[x][y].getObjGrh().setGrhIndex((short) 0);
    }

}
