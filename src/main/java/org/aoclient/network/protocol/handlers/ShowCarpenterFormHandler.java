package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowCarpenterFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        //If //FrmMain.macrotrabajo.Enabled And (MacroBltIndex > 0) Then
        //        Call WriteCraftCarpenter(MacroBltIndex)
        //    Else
        //        frmCarp.Show , //FrmMain
        //    End If

        Logger.debug("handleShowCarpenterForm CARGADO - FALTA TERMINAR!");
    }

}
