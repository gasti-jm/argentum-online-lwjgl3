package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildNewsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(7)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String news = tempBuffer.readCp1252String();
        String guildList = tempBuffer.readCp1252String();
        guildList = tempBuffer.readCp1252String();

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

        buffer.copy(tempBuffer);
        Logger.debug("handleGuildNews Cargado! - FALTA TERMINAR!");
    }

}
