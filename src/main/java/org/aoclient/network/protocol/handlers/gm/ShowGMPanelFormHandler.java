package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class ShowGMPanelFormHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        //frmPanelGm.Show vbModeless, //FrmMain
    }

}
