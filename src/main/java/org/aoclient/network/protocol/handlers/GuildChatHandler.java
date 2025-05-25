package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildChatHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String chat = tempBuffer.readCp1252String();

        //Dim str As String
        //    Dim r As Byte
        //    Dim g As Byte
        //    Dim b As Byte
        //    Dim tmp As Integer
        //    Dim Cont As Integer
        //
        //
        //    chat = Buffer.ReadASCIIString()
        //
        //    If Not DialogosClanes.Activo Then
        //        If InStr(1, chat, "~") Then
        //            str = ReadField(2, chat, 126)
        //            If Val(str) > 255 Then
        //                r = 255
        //            Else
        //                r = Val(str)
        //            End If
        //
        //            str = ReadField(3, chat, 126)
        //            If Val(str) > 255 Then
        //                g = 255
        //            Else
        //                g = Val(str)
        //            End If
        //
        //            str = ReadField(4, chat, 126)
        //            If Val(str) > 255 Then
        //                b = 255
        //            Else
        //                b = Val(str)
        //            End If
        //
        //            Call AddtoRichTextBox(//FrmMain.RecTxt, Left$(chat, InStr(1, chat, "~") - 1), r, g, b, Val(ReadField(5, chat, 126)) <> 0, Val(ReadField(6, chat, 126)) <> 0)
        //        Else
        //            With FontTypes(FontTypeNames.FONTTYPE_GUILDMSG)
        //                Call AddtoRichTextBox(//FrmMain.RecTxt, chat, .red, .green, .blue, .bold, .italic)
        //            End With
        //        End If
        //    Else
        //        Call DialogosClanes.PushBackText(ReadField(1, chat, 126))
        //    End If

        buffer.copy(tempBuffer);
        Logger.debug("handleGuildChat CARGADO - FALTA TERMINAR");
    }

}
