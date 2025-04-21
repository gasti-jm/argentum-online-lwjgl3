package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class UserIndexInServerHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        int userIndex = data.readInteger();
        Logger.debug("No le encontre utilidad a este paquete....");
    }
}
