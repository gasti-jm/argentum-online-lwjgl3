package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowSignalHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(5)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String tmp = buffer.readUTF8String();
        short param = buffer.readInteger();
        // Call InitCartel(tmp, Buffer.ReadInteger())

        data.copy(buffer);
        Logger.debug("handleShowSignal Cargado! - FALTA TERMINAR!");
    }
    
}
