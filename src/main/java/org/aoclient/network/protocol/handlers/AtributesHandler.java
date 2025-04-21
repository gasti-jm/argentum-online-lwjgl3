package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class AtributesHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(6)) return;

        // Remove packet ID
        data.readByte();

        int[] userAtributos = new int[5];
        for (int i = 0; i < 5; i++)
            userAtributos[i] = data.readByte();

        //Dim i As Long
        //
        //    For i = 1 To NUMATRIBUTES
        //        UserAtributos(i) = data.ReadByte()
        //    Next i
        //
        //    'Show them in character creation
        //    If EstadoLogin = E_MODO.Dados Then
        //        With frmCrearPersonaje
        //            If .Visible Then
        //                For i = 1 To NUMATRIBUTES
        //                    .lblAtributos(i).Caption = UserAtributos(i)
        //                Next i
        //
        //                .UpdateStats
        //            End If
        //        End With
        //    Else
        //        LlegaronAtrib = True
        //    End If

        Logger.debug("handleAtributes Cargado! - FALTA TERMINAR!");
    }
    
}
