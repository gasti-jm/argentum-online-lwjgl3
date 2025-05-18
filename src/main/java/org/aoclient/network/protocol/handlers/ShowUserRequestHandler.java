package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowUserRequestHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String recievePeticion = buffer.readCp1252String();

        // Call frmUserRequest.recievePeticion(Buffer.ReadASCIIString())
        //    Call frmUserRequest.Show(vbModeless, //FrmMain)
        //

        data.copy(buffer);
        Logger.debug("handleShowUserRequest Cargado! - FALTA TERMINAR!");
    }

}
