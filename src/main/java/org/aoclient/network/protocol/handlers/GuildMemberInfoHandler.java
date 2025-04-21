package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class GuildMemberInfoHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String guildNames = buffer.readASCIIString();
        String guildMembers = buffer.readASCIIString();


        //With frmGuildMember
        //        'Clear guild's list
        //        .lstClanes.Clear
        //
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        Dim i As Long
        //        For i = 0 To UBound(GuildNames())
        //            Call .lstClanes.AddItem(GuildNames(i))
        //        Next i
        //
        //        'Get list of guild's members
        //        GuildMembers = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //        .lblCantMiembros.Caption = CStr(UBound(GuildMembers()) + 1)
        //
        //        'Empty the list
        //        Call .lstMiembros.Clear
        //
        //        For i = 0 To UBound(GuildMembers())
        //            Call .lstMiembros.AddItem(GuildMembers(i))
        //        Next i
        //
        //        'If we got here then packet is complete, copy data back to original queue
        //        Call data.CopyBuffer(Buffer)
        //
        //        .Show vbModeless, //FrmMain
        //    End With

        data.copyBuffer(buffer);
        Logger.debug("handleGuildMemberInfo Cargado! - FALTA TERMINAR!");
    }

}
