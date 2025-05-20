package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowSignalHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String tmp = tempBuffer.readCp1252String();
        short param = tempBuffer.readInteger();
        // Call InitCartel(tmp, Buffer.ReadInteger())

        buffer.copy(tempBuffer);
        Logger.debug("handleShowSignal Cargado! - FALTA TERMINAR!");
    }

}
