package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class ShowGuildAlignHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        // Remove packet ID
        data.readByte();
        //frmEligeAlineacion.Show vbModeless, //FrmMain
    }

}
