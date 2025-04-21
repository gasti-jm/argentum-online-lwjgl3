package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ShowCarpenterFormHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        //If //FrmMain.macrotrabajo.Enabled And (MacroBltIndex > 0) Then
        //        Call WriteCraftCarpenter(MacroBltIndex)
        //    Else
        //        frmCarp.Show , //FrmMain
        //    End If

        Logger.debug("handleShowCarpenterForm CARGADO - FALTA TERMINAR!");
    }
}
