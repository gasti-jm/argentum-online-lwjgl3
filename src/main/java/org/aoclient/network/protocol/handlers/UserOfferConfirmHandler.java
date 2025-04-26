package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class UserOfferConfirmHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
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
