package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class PeaceProposalsListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String guildList = buffer.readUTF8String();

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

        data.copy(buffer);
        Logger.debug("handlePeaceProposalsList Cargado! - FALTA TERMINAR!");
    }

}
