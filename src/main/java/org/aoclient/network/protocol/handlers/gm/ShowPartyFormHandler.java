package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ShowPartyFormHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        int esPartyLeader = buffer.readByte();
        String members = buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handleShowPartyForm Cargado! - FALTA TERMINAR!");
    }

}
