package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.utils.GameData.charList;

public class SetInvisibleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(4)) return;
        buffer.readByte();

        charList[buffer.readInteger()].setInvisible(buffer.readBoolean());
        Log.debug("handleSetInvisible Cargado!");
    }

}
