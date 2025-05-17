package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class ShowGMPanelFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        //frmPanelGm.Show vbModeless, //FrmMain
    }

}
