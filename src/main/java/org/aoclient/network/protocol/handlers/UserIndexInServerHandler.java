package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UserIndexInServerHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        data.readByte();

        int userIndex = data.readInteger();
        Logger.debug("No le encontre utilidad a este paquete....");
    }
}
