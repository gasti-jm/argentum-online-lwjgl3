package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class ShowMOTDEditionFormHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String txtMotd = buffer.readASCIIString();

        //frmCambiaMotd.txtMotd.Text = Buffer.ReadASCIIString()
        //    frmCambiaMotd.Show , //FrmMain
        //

        data.copyBuffer(buffer);
        Logger.debug("handleShowMOTDEditionForm Cargado! - FALTA TERMINAR!");
    }

}
