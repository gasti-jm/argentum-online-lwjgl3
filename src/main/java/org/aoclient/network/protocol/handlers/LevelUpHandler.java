package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class LevelUpHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        // Remove packet ID
        data.readByte();

        //  variable global:
        // skillPoints += data.readInteger();
        short skillPoints = data.readInteger();

        // //FrmMain.lightskillstar
        Logger.debug("handleLevelUp Cargado! - FALTA TERMINAR!");
    }
    
}
