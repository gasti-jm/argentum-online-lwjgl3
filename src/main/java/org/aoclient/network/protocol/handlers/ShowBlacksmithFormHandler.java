package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowBlacksmithFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        //If //FrmMain.macrotrabajo.Enabled And (MacroBltIndex > 0) Then
        //        Call WriteCraftBlacksmith(MacroBltIndex)
        //    Else
        //        frmHerrero.Show , //FrmMain
        //    End If

        Logger.debug("handleShowBlacksmithForm CARGADO - FALTA TERMINAR!");
    }

}
