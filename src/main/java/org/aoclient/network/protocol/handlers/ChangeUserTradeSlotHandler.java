package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class ChangeUserTradeSlotHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(22)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

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
        buffer.readASCIIString();

        buffer.readInteger();
        buffer.readLong();
        buffer.readInteger();
        buffer.readByte();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readLong();
        buffer.readASCIIString();

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

        data.copyBuffer(buffer);
        Logger.debug("handleChangeUserTradeSlot Cargado! - FALTA TERMINAR!");
    }
}
