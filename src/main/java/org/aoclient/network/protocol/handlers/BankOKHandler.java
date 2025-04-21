package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class BankOKHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        // Remove packet ID
        data.readByte();

        //Dim i As Long
        //
        //    If frmBancoObj.Visible Then
        //
        //        For i = 1 To Inventario.MaxObjs
        //            With Inventario
        //                Call InvBanco(1).SetItem(i, .OBJIndex(i), .Amount(i), _
        //                    .Equipped(i), .GrhIndex(i), .OBJType(i), .MaxHit(i), _
        //                    .MinHit(i), .MaxDef(i), .MinDef(i), .Valor(i), .ItemName(i))
        //            End With
        //        Next i
        //
        //        'Alter order according to if we bought or sold so the labels and grh remain the same
        //        If frmBancoObj.LasActionBuy Then
        //            'frmBancoObj.List1(1).ListIndex = frmBancoObj.LastIndex2
        //            'frmBancoObj.List1(0).ListIndex = frmBancoObj.LastIndex1
        //        Else
        //            'frmBancoObj.List1(0).ListIndex = frmBancoObj.LastIndex1
        //            'frmBancoObj.List1(1).ListIndex = frmBancoObj.LastIndex2
        //        End If
        //
        //        frmBancoObj.NoPuedeMover = False
        //    End If
    }

}
