package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BlindNoMoreHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        //UserCiego = False
        Log.debug("handleBlindNoMore Cargado! - FALTA TERMINAR!");
    }

}
