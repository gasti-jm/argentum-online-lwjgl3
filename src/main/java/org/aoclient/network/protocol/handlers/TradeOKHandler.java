package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

public class TradeOKHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        //If frmComerciar.Visible Then
        //        Dim i As Long
        //
        //        'Update user inventory
        //        For i = 1 To MAX_INVENTORY_SLOTS
        //            ' Agrego o quito un item en su totalidad
        //            If Inventario.OBJIndex(i) <> InvComUsu.OBJIndex(i) Then
        //                With Inventario
        //                    Call InvComUsu.SetItem(i, .OBJIndex(i), _
        //                    .Amount(i), .Equipped(i), .GrhIndex(i), _
        //                    .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
        //                    .Valor(i), .ItemName(i))
        //                End With
        //            ' Vendio o compro cierta cantidad de un item que ya tenia
        //            ElseIf Inventario.Amount(i) <> InvComUsu.Amount(i) Then
        //                Call InvComUsu.ChangeSlotItemAmount(i, Inventario.Amount(i))
        //            End If
        //        Next i
        //
        //        ' Fill Npc inventory
        //        For i = 1 To 20
        //            ' Compraron la totalidad de un item, o vendieron un item que el npc no tenia
        //            If NPCInventory(i).OBJIndex <> InvComNpc.OBJIndex(i) Then
        //                With NPCInventory(i)
        //                    Call InvComNpc.SetItem(i, .OBJIndex, _
        //                    .Amount, 0, .GrhIndex, _
        //                    .OBJType, .MaxHit, .MinHit, .MaxDef, .MinDef, _
        //                    .Valor, .Name)
        //                End With
        //            ' Compraron o vendieron cierta cantidad (no su totalidad)
        //            ElseIf NPCInventory(i).Amount <> InvComNpc.Amount(i) Then
        //                Call InvComNpc.ChangeSlotItemAmount(i, NPCInventory(i).Amount)
        //            End If
        //        Next i
        //
        //    End If
    }
}
