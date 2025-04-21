package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class GuildListHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handleGuildList CARGADO - FALTA TERMINAR!");
    }
    
}
