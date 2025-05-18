package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UserCommerceInitHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        // variable local vb6
        String tradingUserName = data.readCp1252String();

        //' Initialize commerce inventories
        //    Call InvComUsu.Initialize(DirectDraw, frmComerciarUsu.picInvComercio, Inventario.MaxObjs)
        //    Call InvOfferComUsu(0).Initialize(DirectDraw, frmComerciarUsu.picInvOfertaProp, INV_OFFER_SLOTS)
        //    Call InvOfferComUsu(1).Initialize(DirectDraw, frmComerciarUsu.picInvOfertaOtro, INV_OFFER_SLOTS)
        //    Call InvOroComUsu(0).Initialize(DirectDraw, frmComerciarUsu.picInvOroProp, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //    Call InvOroComUsu(1).Initialize(DirectDraw, frmComerciarUsu.picInvOroOfertaProp, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //    Call InvOroComUsu(2).Initialize(DirectDraw, frmComerciarUsu.picInvOroOfertaOtro, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //
        //    'Fill user inventory
        //    For i = 1 To MAX_INVENTORY_SLOTS
        //        If Inventario.OBJIndex(i) <> 0 Then
        //            With Inventario
        //                Call InvComUsu.SetItem(i, .OBJIndex(i), _
        //                .Amount(i), .Equipped(i), .GrhIndex(i), _
        //                .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
        //                .Valor(i), .ItemName(i))
        //            End With
        //        End If
        //    Next i
        //
        //    ' Inventarios de oro
        //    Call InvOroComUsu(0).SetItem(1, ORO_INDEX, UserGLD, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //    Call InvOroComUsu(1).SetItem(1, ORO_INDEX, 0, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //    Call InvOroComUsu(2).SetItem(1, ORO_INDEX, 0, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //
        //
        //    'Set state and show form
        //    Comerciando = True
        //    Call frmComerciarUsu.Show(vbModeless, //FrmMain)

        Logger.debug("handleUserCommerceInit CARGADO - FALTA TERMINAR!");
    }
}
