package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class CharacterInfoHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {

        if (data.checkPacketData(35)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String nombre = buffer.readASCIIString();
        int raza = buffer.readByte();
        int clase = buffer.readByte();
        int genero = buffer.readByte();
        int nivel = buffer.readByte();
        int oro = buffer.readLong();
        int banco = buffer.readLong();
        int reputacion = buffer.readLong();

        String txtPeticiones = buffer.readASCIIString();
        String guildActual = buffer.readASCIIString();
        String txtMiembro = buffer.readASCIIString();

        boolean armada = buffer.readBoolean();
        boolean caos = buffer.readBoolean();

        int ciudadanos = buffer.readLong();
        int criminales = buffer.readLong();

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


        data.copyBuffer(buffer);
        Logger.debug("handleCharacterInfo Cargado! - FALTA TERMINAR!");
    }
    
}
