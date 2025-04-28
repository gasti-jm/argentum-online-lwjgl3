package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class CommerceChatHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();
        int fontSize = buffer.readByte();

        /*
        If InStr(1, chat, "~") Then
        str = ReadField(2, chat, 126)
            If Val(str) > 255 Then
                r = 255
            Else
                r = Val(str)
            End If

            str = ReadField(3, chat, 126)
            If Val(str) > 255 Then
                g = 255
            Else
                g = Val(str)
            End If

            str = ReadField(4, chat, 126)
            If Val(str) > 255 Then
                b = 255
            Else
                b = Val(str)
            End If

        Call AddtoRichTextBox(frmComerciarUsu.CommerceConsole, Left$(chat, InStr(1, chat, "~") - 1), r, g, b, Val(ReadField(5, chat, 126)) <> 0, Val(ReadField(6, chat, 126)) <> 0)
    Else
        With FontTypes(FontIndex)
            Call AddtoRichTextBox(frmComerciarUsu.CommerceConsole, chat, .red, .green, .blue, .bold, .italic)
        End With
    End If
         */


        data.copyBuffer(buffer);

        Logger.debug("handleCommerceChat CARGADO - FALTA TERMINAR!");
    }
}
