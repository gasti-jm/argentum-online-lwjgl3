package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class UpdateBankGoldHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(5)) return;
        buffer.readByte();

        int bankGold = buffer.readLong();
        //frmBancoObj.lblUserGld.Caption = incomingData.ReadLong
        Logger.debug("handleUpdateBankGold CARGADO - FALTA TERMINAR!");
    }

}
