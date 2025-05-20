package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BlindHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        //UserCiego = True
        Logger.debug("handleBlind Cargado! - FALTA TERMINAR!");
    }

}
