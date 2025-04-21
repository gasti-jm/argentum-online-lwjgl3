package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class RestOKHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        //UserDescansar = Not UserDescansar
        Logger.debug("handleCarpenterObjects Cargado! - FALTA TERMINAR!");
    }

}
