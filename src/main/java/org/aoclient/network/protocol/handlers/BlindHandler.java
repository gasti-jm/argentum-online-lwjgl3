package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class BlindHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        //UserCiego = True
        Logger.debug("handleBlind Cargado! - FALTA TERMINAR!");
    }

}
