package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class SendNightHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(2)) return;
        buffer.readByte();

        boolean tBool = buffer.readBoolean();
        Log.debug("handleSendNight Cargado! - FALTA TERMINAR!");
    }

}
