package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class BlacksmithArmorsHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {

        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        short count = buffer.readInteger();

        for (int i = 0; i < count; i++) {
            buffer.readASCIIString();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
        }

        //ReDim ArmadurasHerrero(Count) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ArmadurasHerrero(i)
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
        //    j = UBound(HerreroMejorar)
        //
        //    For i = 1 To Count
        //        With ArmadurasHerrero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ArmadurasHerrero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve HerreroMejorar(j) As tItemsConstruibles
        //
        //                        HerreroMejorar(j).Name = .Name
        //                        HerreroMejorar(j).GrhIndex = .GrhIndex
        //                        HerreroMejorar(j).OBJIndex = .OBJIndex
        //                        HerreroMejorar(j).UpgradeName = ArmadurasHerrero(k).Name
        //                        HerreroMejorar(j).UpgradeGrhIndex = ArmadurasHerrero(k).GrhIndex
        //                        HerreroMejorar(j).LinH = ArmadurasHerrero(k).LinH - .LinH * 0.85
        //                        HerreroMejorar(j).LinP = ArmadurasHerrero(k).LinP - .LinP * 0.85
        //                        HerreroMejorar(j).LinO = ArmadurasHerrero(k).LinO - .LinO * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        data.copyBuffer(buffer);
        Logger.debug("handleBlacksmithArmors Cargado! - FALTA TERMINAR!");
    }
    
}
