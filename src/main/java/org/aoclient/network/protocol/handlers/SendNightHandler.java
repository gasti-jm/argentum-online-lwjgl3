package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class SendNightHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2)) return;

        // Remove packet ID
        data.readByte();

        boolean tBool = data.readBoolean();
        Logger.debug("handleSendNight Cargado! - FALTA TERMINAR!");
    }
}
