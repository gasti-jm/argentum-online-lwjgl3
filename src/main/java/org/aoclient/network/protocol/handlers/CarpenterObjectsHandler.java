package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class CarpenterObjectsHandler implements PacketHandler {

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
        }

        //ReDim ObjCarpintero(Count) As tItemsConstruibles
        //    ReDim CarpinteroMejorar(0) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ObjCarpintero(i)
        //            .Name = Buffer.ReadASCIIString()        'Get the object's name
        //            .GrhIndex = Buffer.ReadInteger()
        //            .Madera = Buffer.ReadInteger()          'The wood needed
        //            .MaderaElfica = Buffer.ReadInteger()    'The elfic wood needed
        //            .OBJIndex = Buffer.ReadInteger()
        //            .Upgrade = Buffer.ReadInteger()
        //        End With
        //    Next i
        //
        //    With frmCarp
        //        ' Inicializo los inventarios
        //        Call InvMaderasCarpinteria(1).Initialize(DirectDraw, .picMaderas0, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(2).Initialize(DirectDraw, .picMaderas1, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(3).Initialize(DirectDraw, .picMaderas2, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(4).Initialize(DirectDraw, .picMaderas3, 2, , , , , , False)
        //
        //        Call .HideExtraControls(Count)
        //        Call .RenderList(1)
        //    End With
        //
        //    For i = 1 To Count
        //        With ObjCarpintero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ObjCarpintero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve CarpinteroMejorar(j) As tItemsConstruibles
        //
        //                        CarpinteroMejorar(j).Name = .Name
        //                        CarpinteroMejorar(j).GrhIndex = .GrhIndex
        //                        CarpinteroMejorar(j).OBJIndex = .OBJIndex
        //                        CarpinteroMejorar(j).UpgradeName = ObjCarpintero(k).Name
        //                        CarpinteroMejorar(j).UpgradeGrhIndex = ObjCarpintero(k).GrhIndex
        //                        CarpinteroMejorar(j).Madera = ObjCarpintero(k).Madera - .Madera * 0.85
        //                        CarpinteroMejorar(j).MaderaElfica = ObjCarpintero(k).MaderaElfica - .MaderaElfica * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        buffer.copy(tempBuffer);
        Logger.debug("handleCarpenterObjects Cargado! - FALTA TERMINAR!");
    }

}
