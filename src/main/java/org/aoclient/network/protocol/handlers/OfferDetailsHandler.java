package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class OfferDetailsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String recievePeticion = tempBuffer.readCp1252String();

        buffer.copy(tempBuffer);
        Logger.debug("handleOfferDetails Cargado! - FALTA TERMINAR!");
    }

}
