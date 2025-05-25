package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class PeaceProposalsListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String guildList = tempBuffer.readCp1252String();

        //Dim guildList() As String
        //    Dim i As Long
        //
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    Call frmPeaceProp.lista.Clear
        //    For i = 0 To UBound(guildList())
        //        Call frmPeaceProp.lista.AddItem(guildList(i))
        //    Next i
        //
        //    frmPeaceProp.ProposalType = TIPO_PROPUESTA.PAZ
        //    Call frmPeaceProp.Show(vbModeless, //FrmMain)

        buffer.copy(tempBuffer);
        Logger.debug("handlePeaceProposalsList Cargado! - FALTA TERMINAR!");
    }

}
