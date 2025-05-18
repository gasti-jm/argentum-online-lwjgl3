package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildListHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readCp1252String();

        //With frmGuildAdm
        //        'Clear guild's list
        //        .guildslist.Clear
        //
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        Dim i As Long
        //        For i = 0 To UBound(GuildNames())
        //            Call .guildslist.AddItem(GuildNames(i))
        //        Next i
        //
        //        'If we got here then packet is complete, copy data back to original queue
        //        Call data.CopyBuffer(Buffer)
        //
        //        .Show vbModeless, //FrmMain
        //    End With

        data.copy(buffer);
        Logger.debug("handleGuildList CARGADO - FALTA TERMINAR!");
    }
    
}
