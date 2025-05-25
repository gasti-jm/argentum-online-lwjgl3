package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class AlianceProposalsListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String vsGuildList = tempBuffer.readCp1252String();

        //Dim vsGuildList() As String
        //    Dim i As Long
        //
        //    vsGuildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    Call frmPeaceProp.lista.Clear
        //    For i = 0 To UBound(vsGuildList())
        //        Call frmPeaceProp.lista.AddItem(vsGuildList(i))
        //    Next i
        //
        //    frmPeaceProp.ProposalType = TIPO_PROPUESTA.ALIANZA
        //    Call frmPeaceProp.Show(vbModeless, //FrmMain)

        buffer.copy(tempBuffer);
        Logger.debug("handleAlianceProposalsList Cargado! - FALTA TERMINAR!");
    }

}
