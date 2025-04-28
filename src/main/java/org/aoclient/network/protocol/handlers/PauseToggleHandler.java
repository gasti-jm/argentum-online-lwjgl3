package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class PauseToggleHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        // Remove packet ID
        data.readByte();
        //pausa = Not pausa
        Logger.debug("handlePauseToggle CARGADO - FALTA TERMINAR!");
    }

}
