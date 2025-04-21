package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class OfferDetailsHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String recievePeticion = buffer.readASCIIString();

        data.copyBuffer(buffer);
        Logger.debug("handleOfferDetails Cargado! - FALTA TERMINAR!");
    }
}
