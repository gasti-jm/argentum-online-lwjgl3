package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class SendNightHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(2)) return;

        // Remove packet ID
        data.readByte();

        boolean tBool = data.readBoolean();
        Logger.debug("handleSendNight Cargado! - FALTA TERMINAR!");
    }
}
