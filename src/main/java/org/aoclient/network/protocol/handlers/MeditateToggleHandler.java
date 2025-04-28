package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class MeditateToggleHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        // Remove packet ID
        data.readByte();

        //UserMeditar = Not UserMeditar
        Logger.debug("handleMeditateToggle Cargado! - FALTA TERMINAR!");
    }
}
