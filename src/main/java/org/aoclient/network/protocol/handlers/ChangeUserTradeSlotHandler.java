package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ChangeUserTradeSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(22)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int offerSlot = tempBuffer.readByte();

        tempBuffer.readInteger();
        tempBuffer.readLong();
        tempBuffer.readInteger();
        tempBuffer.readByte();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readLong();
        tempBuffer.readCp1252String();

        tempBuffer.readInteger();
        tempBuffer.readLong();
        tempBuffer.readInteger();
        tempBuffer.readByte();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readInteger();
        tempBuffer.readLong();
        tempBuffer.readCp1252String();

        //OfferSlot = Buffer.ReadByte
        //
        //    With Buffer
        //        If OfferSlot = GOLD_OFFER_SLOT Then
        //            Call InvOroComUsu(2).SetItem(1, .ReadInteger(), .ReadLong(), 0, _
        //                                            .ReadInteger(), .ReadByte(), .ReadInteger(), _
        //                                            .ReadInteger(), .ReadInteger(), .ReadInteger(), .ReadLong(), .ReadASCIIString())
        //        Else
        //            Call InvOfferComUsu(1).SetItem(OfferSlot, .ReadInteger(), .ReadLong(), 0, _
        //                                            .ReadInteger(), .ReadByte(), .ReadInteger(), _
        //                                            .ReadInteger(), .ReadInteger(), .ReadInteger(), .ReadLong(), .ReadASCIIString())
        //        End If
        //    End With
        //
        //    Call frmComerciarUsu.PrintCommerceMsg(TradingUserName & " ha modificado su oferta.", FontTypeNames.FONTTYPE_VENENO)

        buffer.copy(tempBuffer);
        Logger.debug("handleChangeUserTradeSlot Cargado! - FALTA TERMINAR!");
    }

}
