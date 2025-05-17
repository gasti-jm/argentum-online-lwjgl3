package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.initGrh;
import static org.aoclient.engine.utils.GameData.mapData;

public class ObjectCreateHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(5)) return;

        data.readByte();

        int x = data.readByte();
        int y = data.readByte();
        short grhIndex = data.readInteger();

        mapData[x][y].getObjGrh().setGrhIndex(grhIndex);
        initGrh(mapData[x][y].getObjGrh(), mapData[x][y].getObjGrh().getGrhIndex(), true);
    }
    
}
