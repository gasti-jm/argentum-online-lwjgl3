package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BlindNoMoreHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();

        //UserCiego = False
        Logger.debug("handleBlindNoMore Cargado! - FALTA TERMINAR!");
    }

}
