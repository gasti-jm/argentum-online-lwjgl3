package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class DumbNoMoreHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        // userEstupido = false;
        Logger.debug("handleDumbNoMore Cargado! - FALTA TERMINAR!");
    }
}
