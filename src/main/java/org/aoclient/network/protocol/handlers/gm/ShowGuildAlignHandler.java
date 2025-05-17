package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class ShowGuildAlignHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();
        //frmEligeAlineacion.Show vbModeless, //FrmMain
    }

}
