package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.types.NickColorType;
import org.tinylog.Logger;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateTagAndStatusHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(6)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        short charIndex = tempBuffer.readInteger();
        int nickColor = tempBuffer.readByte();
        String userTag = tempBuffer.readCp1252String();


        charList[charIndex].setCriminal((nickColor & NickColorType.CRIMINAL.getId()) != 0);
        charList[charIndex].setAttackable((nickColor & NickColorType.ATACABLE.getId()) != 0);
        charList[charIndex].setName(userTag);

        buffer.copy(tempBuffer);
    }

}
