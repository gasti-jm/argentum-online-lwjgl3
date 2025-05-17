package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UserOfferConfirmHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        data.readByte();

        // With frmComerciarUsu
        //        ' Now he can accept the offer or reject it
        //        .HabilitarAceptarRechazar True
        //
        //        .PrintCommerceMsg TradingUserName & " ha confirmado su oferta!", FontTypeNames.FONTTYPE_CONSE
        //    End With

        Logger.debug("handleUserOfferConfirm CARGADO - FALTA TERMINAR!");
    }
}
