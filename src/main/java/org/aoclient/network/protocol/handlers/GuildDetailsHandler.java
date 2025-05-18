package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildDetailsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(26)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        buffer.readCp1252String();
        buffer.readCp1252String();
        buffer.readCp1252String();
        buffer.readCp1252String();
        buffer.readCp1252String();
        buffer.readInteger();

        buffer.readBoolean();

        buffer.readCp1252String();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readCp1252String();

        buffer.readCp1252String();

        buffer.readCp1252String();

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

        data.copy(buffer);
        Logger.debug("handleGuildDetails Cargado! - FALTA TERMINAR!");
    }
}
