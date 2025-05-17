package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UpdateTagAndStatusHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(6)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        short charIndex = buffer.readInteger();
        int nickColor = buffer.readByte();
        String userTag = buffer.readUTF8String();

        //Dim CharIndex As Integer
        //    Dim NickColor As Byte
        //    Dim UserTag As String
        //
        //    CharIndex = Buffer.ReadInteger()
        //    NickColor = Buffer.ReadByte()
        //    UserTag = Buffer.ReadASCIIString()
        //
        //    'Update char status adn tag!
        //    With charlist(CharIndex)
        //        If (NickColor And eNickColor.ieCriminal) <> 0 Then
        //            .Criminal = 1
        //        Else
        //            .Criminal = 0
        //        End If
        //
        //        .Atacable = (NickColor And eNickColor.ieAtacable) <> 0
        //
        //        .Nombre = UserTag
        //    End With

        data.copy(buffer);
        Logger.debug("handleUpdateTagAndStatus Cargado! - FALTA TERMINAR!");
    }

}
