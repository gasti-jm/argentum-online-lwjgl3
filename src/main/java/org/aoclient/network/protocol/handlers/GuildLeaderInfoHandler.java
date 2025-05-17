package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildLeaderInfoHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(9)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readUTF8String();
        guildNames = buffer.readUTF8String();
        String txtGuildNews = buffer.readUTF8String();
        String list = buffer.readUTF8String();

        //With frmGuildLeader
        //        'Get list of existing guilds
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        'Empty the list
        //        Call .guildslist.Clear
        //
        //        For i = 0 To UBound(GuildNames())
        //            Call .guildslist.AddItem(GuildNames(i))
        //        Next i
        //
        //        'Get list of guild's members
        //        GuildMembers = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //        .Miembros.Caption = CStr(UBound(GuildMembers()) + 1)
        //
        //        'Empty the list
        //        Call .members.Clear
        //
        //        For i = 0 To UBound(GuildMembers())
        //            Call .members.AddItem(GuildMembers(i))
        //        Next i
        //
        //        .txtguildnews = Buffer.ReadASCIIString()
        //
        //        'Get list of join requests
        //        List = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        'Empty the list
        //        Call .solicitudes.Clear
        //
        //        For i = 0 To UBound(List())
        //            Call .solicitudes.AddItem(List(i))
        //        Next i
        //
        //        .Show , //FrmMain
        //    End With

        data.copy(buffer);
        Logger.debug("handleGuildLeaderInfo Cargado! - FALTA TERMINAR!");
    }
    
}
