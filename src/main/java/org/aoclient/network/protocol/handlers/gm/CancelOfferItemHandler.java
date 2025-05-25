package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class CancelOfferItemHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        int slot = buffer.readByte();

        //With InvOfferComUsu(0)
        //        Amount = .Amount(slot)
        //
        //        ' No tiene sentido que se quiten 0 unidades
        //        If Amount <> 0 Then
        //            ' Actualizo el inventario general
        //            Call frmComerciarUsu.UpdateInvCom(.OBJIndex(slot), Amount)
        //
        //            ' Borro el item
        //            Call .SetItem(slot, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
        //        End If
        //    End With
        //
        //    ' Si era el único ítem de la oferta, no puede confirmarla
        //    If Not frmComerciarUsu.HasAnyItem(InvOfferComUsu(0)) And _
        //        Not frmComerciarUsu.HasAnyItem(InvOroComUsu(1)) Then Call frmComerciarUsu.HabilitarConfirmar(False)
        //
        //    With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call frmComerciarUsu.PrintCommerceMsg("¡No puedes comerciar ese objeto!", FontTypeNames.FONTTYPE_INFO)
        //    End With
    }

}
