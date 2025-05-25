package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class CharacterInfoHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(35)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String nombre = tempBuffer.readCp1252String();
        int raza = tempBuffer.readByte();
        int clase = tempBuffer.readByte();
        int genero = tempBuffer.readByte();
        int nivel = tempBuffer.readByte();
        int oro = tempBuffer.readLong();
        int banco = tempBuffer.readLong();
        int reputacion = tempBuffer.readLong();

        String txtPeticiones = tempBuffer.readCp1252String();
        String guildActual = tempBuffer.readCp1252String();
        String txtMiembro = tempBuffer.readCp1252String();

        boolean armada = tempBuffer.readBoolean();
        boolean caos = tempBuffer.readBoolean();

        int ciudadanos = tempBuffer.readLong();
        int criminales = tempBuffer.readLong();

        //With frmCharInfo
        //        If .frmType = CharInfoFrmType.frmMembers Then
        //            .imgRechazar.Visible = False
        //            .imgAceptar.Visible = False
        //            .imgEchar.Visible = True
        //            .imgPeticion.Visible = False
        //        Else
        //            .imgRechazar.Visible = True
        //            .imgAceptar.Visible = True
        //            .imgEchar.Visible = False
        //            .imgPeticion.Visible = True
        //        End If
        //
        //        .Nombre.Caption = Buffer.ReadASCIIString()
        //        .Raza.Caption = ListaRazas(Buffer.ReadByte())
        //        .Clase.Caption = ListaClases(Buffer.ReadByte())
        //
        //        If Buffer.ReadByte() = 1 Then
        //            .Genero.Caption = "Hombre"
        //        Else
        //            .Genero.Caption = "Mujer"
        //        End If
        //
        //        .Nivel.Caption = Buffer.ReadByte()
        //        .Oro.Caption = Buffer.ReadLong()
        //        .Banco.Caption = Buffer.ReadLong()
        //
        //        Dim reputation As Long
        //        reputation = Buffer.ReadLong()
        //
        //        .reputacion.Caption = reputation
        //
        //        .txtPeticiones.Text = Buffer.ReadASCIIString()
        //        .guildactual.Caption = Buffer.ReadASCIIString()
        //        .txtMiembro.Text = Buffer.ReadASCIIString()
        //
        //        Dim armada As Boolean
        //        Dim caos As Boolean
        //
        //        armada = Buffer.ReadBoolean()
        //        caos = Buffer.ReadBoolean()
        //
        //        If armada Then
        //            .ejercito.Caption = "Armada Real"
        //        ElseIf caos Then
        //            .ejercito.Caption = "Legión Oscura"
        //        End If
        //
        //        .Ciudadanos.Caption = CStr(Buffer.ReadLong())
        //        .criminales.Caption = CStr(Buffer.ReadLong())
        //
        //        If reputation > 0 Then
        //            .status.Caption = " Ciudadano"
        //            .status.ForeColor = vbBlue
        //        Else
        //            .status.Caption = " Criminal"
        //            .status.ForeColor = vbRed
        //        End If
        //
        //        Call .Show(vbModeless, //FrmMain)
        //    End With

        buffer.copy(tempBuffer);
        Logger.debug("handleCharacterInfo Cargado! - FALTA TERMINAR!");
    }

}
