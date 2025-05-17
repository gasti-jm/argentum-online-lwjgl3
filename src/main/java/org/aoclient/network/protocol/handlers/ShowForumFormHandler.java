package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowForumFormHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        int privilegios = data.readByte();
        int canPostSticky = data.readByte();

        //frmForo.Privilegios = data.ReadByte
        //    frmForo.CanPostSticky = data.ReadByte
        //
        //    If Not MirandoForo Then
        //        frmForo.Show , //FrmMain
        //    End If
        Logger.debug("handleShowForumForm Cargado! - FALTA TERMINAR!");
    }
}
