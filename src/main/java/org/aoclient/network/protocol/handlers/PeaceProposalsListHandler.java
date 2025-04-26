package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class PeaceProposalsListHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String guildList = buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handlePeaceProposalsList Cargado! - FALTA TERMINAR!");
    }

}
