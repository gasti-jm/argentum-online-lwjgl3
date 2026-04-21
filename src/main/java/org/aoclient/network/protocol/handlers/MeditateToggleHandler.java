package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class MeditateToggleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        //UserMeditar = Not UserMeditar
        Log.debug("handleMeditateToggle Cargado! - FALTA TERMINAR!");
    }

}
