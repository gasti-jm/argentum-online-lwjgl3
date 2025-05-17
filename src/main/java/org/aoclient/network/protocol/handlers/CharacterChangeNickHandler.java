package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class CharacterChangeNickHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(5)) return;
        data.readByte();
        charList[data.readInteger()].setName(data.readUTF8String());
    }

}
