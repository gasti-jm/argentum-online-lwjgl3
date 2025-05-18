package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ChangeUserTradeSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(22)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        int offerSlot = buffer.readByte();

        buffer.readInteger();
        buffer.readLong();
        buffer.readInteger();
        buffer.readByte();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readLong();
        buffer.readCp1252String();

        buffer.readInteger();
        buffer.readLong();
        buffer.readInteger();
        buffer.readByte();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readLong();
        buffer.readCp1252String();

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

        data.copy(buffer);
        Logger.debug("handleChangeUserTradeSlot Cargado! - FALTA TERMINAR!");
    }
}
