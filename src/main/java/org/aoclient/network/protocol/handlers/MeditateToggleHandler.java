package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class MeditateToggleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        //UserMeditar = Not UserMeditar
        Logger.debug("handleMeditateToggle Cargado! - FALTA TERMINAR!");
    }
}
