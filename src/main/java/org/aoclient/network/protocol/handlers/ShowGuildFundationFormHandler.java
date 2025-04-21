package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ShowGuildFundationFormHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        //CreandoClan = True
        //    frmGuildFoundation.Show , //FrmMain
        Logger.debug("handleShowGuildFundationForm Cargado! - FALTA TERMINAR!");
    }

}
