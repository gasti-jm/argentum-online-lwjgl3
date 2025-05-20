package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BlacksmithWeaponsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        short count = tempBuffer.readInteger();

        for (int i = 0; i < count; i++) {
            tempBuffer.readCp1252String();
            tempBuffer.readInteger();
            tempBuffer.readInteger();
            tempBuffer.readInteger();
            tempBuffer.readInteger();
            tempBuffer.readInteger();
            tempBuffer.readInteger();
        }

        //ReDim ArmasHerrero(Count) As tItemsConstruibles
        //    ReDim HerreroMejorar(0) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ArmasHerrero(i)
        //            .Name = Buffer.ReadASCIIString()    'Get the object's name
        //            .GrhIndex = Buffer.ReadInteger()
        //            .LinH = Buffer.ReadInteger()        'The iron needed
        //            .LinP = Buffer.ReadInteger()        'The silver needed
        //            .LinO = Buffer.ReadInteger()        'The gold needed
        //            .OBJIndex = Buffer.ReadInteger()
        //            .Upgrade = Buffer.ReadInteger()
        //        End With
        //    Next i
        //
        //    With frmHerrero
        //        ' Inicializo los inventarios
        //        Call InvLingosHerreria(1).Initialize(DirectDraw, .picLingotes0, 3, , , , , , False)
        //        Call InvLingosHerreria(2).Initialize(DirectDraw, .picLingotes1, 3, , , , , , False)
        //        Call InvLingosHerreria(3).Initialize(DirectDraw, .picLingotes2, 3, , , , , , False)
        //        Call InvLingosHerreria(4).Initialize(DirectDraw, .picLingotes3, 3, , , , , , False)
        //
        //        Call .HideExtraControls(Count)
        //        Call .RenderList(1, True)
        //    End With
        //
        //    For i = 1 To Count
        //        With ArmasHerrero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ArmasHerrero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve HerreroMejorar(j) As tItemsConstruibles
        //
        //                        HerreroMejorar(j).Name = .Name
        //                        HerreroMejorar(j).GrhIndex = .GrhIndex
        //                        HerreroMejorar(j).OBJIndex = .OBJIndex
        //                        HerreroMejorar(j).UpgradeName = ArmasHerrero(k).Name
        //                        HerreroMejorar(j).UpgradeGrhIndex = ArmasHerrero(k).GrhIndex
        //                        HerreroMejorar(j).LinH = ArmasHerrero(k).LinH - .LinH * 0.85
        //                        HerreroMejorar(j).LinP = ArmasHerrero(k).LinP - .LinP * 0.85
        //                        HerreroMejorar(j).LinO = ArmasHerrero(k).LinO - .LinO * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        buffer.copy(tempBuffer);
        Logger.debug("handleBlacksmithWeapons Cargado! - FALTA TERMINAR!");
    }

}
