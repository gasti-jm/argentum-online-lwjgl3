package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class AddForumMessageHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(8)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();


        int forumType = buffer.readByte();
        String title = buffer.readASCIIString();
        String autor = buffer.readASCIIString();
        String message = buffer.readASCIIString();

        //If Not frmForo.ForoLimpio Then
        //        clsForos.ClearForums
        //        frmForo.ForoLimpio = True
        //    End If
        //
        //    Call clsForos.AddPost(ForumAlignment(ForumType), Title, Author, Message, EsAnuncio(ForumType))


        data.copyBuffer(buffer);
        Logger.debug("handleAddForumMessage Cargado! - FALTA TERMINAR!");
    }
}
