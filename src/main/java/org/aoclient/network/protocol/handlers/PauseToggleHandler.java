package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class PauseToggleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();
        //pausa = Not pausa
        Logger.debug("handlePauseToggle CARGADO - FALTA TERMINAR!");
    }

}
