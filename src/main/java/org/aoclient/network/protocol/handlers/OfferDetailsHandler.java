package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class OfferDetailsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String recievePeticion = buffer.readCp1252String();

        data.copy(buffer);
        Logger.debug("handleOfferDetails Cargado! - FALTA TERMINAR!");
    }
}
