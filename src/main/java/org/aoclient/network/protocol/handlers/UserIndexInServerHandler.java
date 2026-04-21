package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UserIndexInServerHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        int userIndex = buffer.readInteger();
        Log.debug("No le encontre utilidad a este paquete....");
    }

}
