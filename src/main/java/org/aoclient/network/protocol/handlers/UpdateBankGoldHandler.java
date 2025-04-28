package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class UpdateBankGoldHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;
        data.readByte();
        int bankGold = data.readLong();
        //frmBancoObj.lblUserGld.Caption = incomingData.ReadLong
        Logger.debug("handleUpdateBankGold CARGADO - FALTA TERMINAR!");
    }
}
