package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class DumbNoMoreHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        // Remove packet ID
        data.readByte();

        // userEstupido = false;
        Logger.debug("handleDumbNoMore Cargado! - FALTA TERMINAR!");
    }
}
