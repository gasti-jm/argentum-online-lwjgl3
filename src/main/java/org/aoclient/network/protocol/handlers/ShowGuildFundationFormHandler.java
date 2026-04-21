package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ShowGuildFundationFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        //CreandoClan = True
        //    frmGuildFoundation.Show , //FrmMain
        Log.debug("handleShowGuildFundationForm Cargado! - FALTA TERMINAR!");
    }

}
