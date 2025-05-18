package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class ShowPartyFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(4)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        int esPartyLeader = buffer.readByte();
        String members = buffer.readCp1252String();

        //Dim members() As String
        //    Dim i As Long
        //
        //    EsPartyLeader = CBool(Buffer.ReadByte())
        //
        //    members = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //    For i = 0 To UBound(members())
        //        Call frmParty.lstMembers.AddItem(members(i))
        //    Next i
        //
        //    frmParty.lblTotalExp.Caption = Buffer.ReadLong
        //    frmParty.Show , //FrmMain

        data.copy(buffer);
        Logger.debug("handleShowPartyForm Cargado! - FALTA TERMINAR!");
    }

}
