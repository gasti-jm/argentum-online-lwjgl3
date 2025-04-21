package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class UpdateTagAndStatusHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(6)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        short charIndex = buffer.readInteger();
        int nickColor = buffer.readByte();
        String userTag = buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handleUpdateTagAndStatus Cargado! - FALTA TERMINAR!");
    }

}
