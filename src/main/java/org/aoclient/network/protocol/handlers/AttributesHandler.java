package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Attributes;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class AttributesHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(6)) return;
        buffer.readByte();

        int[] attributes = new int[E_Attributes.values().length];
        for (int i = 0; i < 5; i++)
            attributes[i] = buffer.readByte();

        User.INSTANCE.setAttributes(attributes);
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
