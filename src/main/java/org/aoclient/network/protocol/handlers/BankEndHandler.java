package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class BankEndHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        /*
            Set InvBanco(0) = Nothing
            Set InvBanco(1) = Nothing

            Unload frmBancoObj
            Comerciando = False
         */

        Logger.debug("handleBankEnd CARGADO - FALTA TERMINAR!");
    }
}
