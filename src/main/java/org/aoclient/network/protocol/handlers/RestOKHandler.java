package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class RestOKHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();

        //UserDescansar = Not UserDescansar
        Logger.debug("handleCarpenterObjects Cargado! - FALTA TERMINAR!");
    }

}
