package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class DumbNoMoreHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        // userEstupido = false;
        Log.debug("handleDumbNoMore Cargado! - FALTA TERMINAR!");
    }

}
