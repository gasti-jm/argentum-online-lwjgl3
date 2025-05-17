package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildChatHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        // 1. Crean un buffer temporal
        PacketBuffer buffer = new PacketBuffer();
        // 2. Copian los datos (bytes) del buffer original
        buffer.copy(data);
        // 3. Leen y procesa los bytes
        buffer.readByte();

        String chat = buffer.readUTF8String();

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

        // 4. Finalmente, copian el buffer modificado de vuelta al original
        data.copy(buffer);
        Logger.debug("handleGuildChat CARGADO - FALTA TERMINAR");
    }

}
