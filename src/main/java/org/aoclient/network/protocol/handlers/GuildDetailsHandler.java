package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class GuildDetailsHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(26)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readInteger();

        buffer.readBoolean();

        buffer.readASCIIString();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readASCIIString();

        buffer.readASCIIString();

        buffer.readASCIIString();

        //With frmGuildBrief
        //        .imgDeclararGuerra.Visible = .EsLeader
        //        .imgOfrecerAlianza.Visible = .EsLeader
        //        .imgOfrecerPaz.Visible = .EsLeader
        //
        //        .Nombre.Caption = Buffer.ReadASCIIString()
        //        .fundador.Caption = Buffer.ReadASCIIString()
        //        .creacion.Caption = Buffer.ReadASCIIString()
        //        .lider.Caption = Buffer.ReadASCIIString()
        //        .web.Caption = Buffer.ReadASCIIString()
        //        .Miembros.Caption = Buffer.ReadInteger()
        //
        //        If Buffer.ReadBoolean() Then
        //            .eleccion.Caption = "ABIERTA"
        //        Else
        //            .eleccion.Caption = "CERRADA"
        //        End If
        //
        //        .lblAlineacion.Caption = Buffer.ReadASCIIString()
        //        .Enemigos.Caption = Buffer.ReadInteger()
        //        .Aliados.Caption = Buffer.ReadInteger()
        //        .antifaccion.Caption = Buffer.ReadASCIIString()
        //
        //        Dim codexStr() As String
        //        Dim i As Long
        //
        //        codexStr = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        For i = 0 To 7
        //            .Codex(i).Caption = codexStr(i)
        //        Next i
        //
        //        .Desc.Text = Buffer.ReadASCIIString()
        //    End With

        data.copyBuffer(buffer);
        Logger.debug("handleGuildDetails Cargado! - FALTA TERMINAR!");
    }
}
