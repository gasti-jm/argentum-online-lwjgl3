package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class GuildNewsHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(7)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String news = buffer.readASCIIString();
        String guildList = buffer.readASCIIString();
        guildList = buffer.readASCIIString();

        //'Get news' string
        //    frmGuildNews.news = Buffer.ReadASCIIString()
        //
        //    'Get Enemy guilds list
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(guildList)
        //        sTemp = frmGuildNews.txtClanesGuerra.Text
        //        frmGuildNews.txtClanesGuerra.Text = sTemp & guildList(i) & vbCrLf
        //    Next i
        //
        //    'Get Allied guilds list
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(guildList)
        //        sTemp = frmGuildNews.txtClanesAliados.Text
        //        frmGuildNews.txtClanesAliados.Text = sTemp & guildList(i) & vbCrLf
        //    Next i
        //
        //    If ClientSetup.bGuildNews Then frmGuildNews.Show vbModeless, //FrmMain

        data.copyBuffer(buffer);
        Logger.debug("handleGuildNews Cargado! - FALTA TERMINAR!");
    }
}
