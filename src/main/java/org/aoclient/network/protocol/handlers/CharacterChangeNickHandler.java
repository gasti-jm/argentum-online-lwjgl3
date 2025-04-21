package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

import static org.aoclient.engine.utils.GameData.charList;

public class CharacterChangeNickHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;
        data.readByte();
        charList[data.readInteger()].setName(data.readASCIIString());
    }

}
