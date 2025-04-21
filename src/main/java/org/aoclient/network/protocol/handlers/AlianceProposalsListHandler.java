package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class AlianceProposalsListHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String vsGuildList = buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handleAlianceProposalsList Cargado! - FALTA TERMINAR!");
    }
}
