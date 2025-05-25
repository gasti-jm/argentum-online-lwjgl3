package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class AddForumMessageHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(8)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int forumType = tempBuffer.readByte();
        String title = tempBuffer.readCp1252String();
        String autor = tempBuffer.readCp1252String();
        String message = tempBuffer.readCp1252String();

        //If Not frmForo.ForoLimpio Then
        //        clsForos.ClearForums
        //        frmForo.ForoLimpio = True
        //    End If
        //
        //    Call clsForos.AddPost(ForumAlignment(ForumType), Title, Author, Message, EsAnuncio(ForumType))

        buffer.copy(tempBuffer);
        Logger.debug("handleAddForumMessage Cargado! - FALTA TERMINAR!");
    }

}
