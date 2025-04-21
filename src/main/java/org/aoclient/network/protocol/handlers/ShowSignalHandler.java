package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ShowSignalHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String tmp = buffer.readASCIIString();
        short param = buffer.readInteger();
        // Call InitCartel(tmp, Buffer.ReadInteger())

        data.copyBuffer(buffer);
        Logger.debug("handleShowSignal Cargado! - FALTA TERMINAR!");
    }
    
}
