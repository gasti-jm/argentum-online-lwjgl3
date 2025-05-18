package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class AddForumMessageHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(8)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        int forumType = buffer.readByte();
        String title = buffer.readCp1252String();
        String autor = buffer.readCp1252String();
        String message = buffer.readCp1252String();

        //If Not frmForo.ForoLimpio Then
        //        clsForos.ClearForums
        //        frmForo.ForoLimpio = True
        //    End If
        //
        //    Call clsForos.AddPost(ForumAlignment(ForumType), Title, Author, Message, EsAnuncio(ForumType))


        data.copy(buffer);
        Logger.debug("handleAddForumMessage Cargado! - FALTA TERMINAR!");
    }
}
