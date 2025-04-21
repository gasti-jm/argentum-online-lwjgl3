package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class SendSkillsHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2 + 20 * 2)) return;

        // Remove packet ID
        data.readByte();

        // variables globales
        int userClase = data.readByte();
        int userSkills[] = new int[20];
        int porcentajeSkills[] = new int[20];


        for (int i = 0; i < 20; i++) {
            userSkills[i] = data.readByte();
            porcentajeSkills[i] = data.readByte();
        }

        // LlegaronSkills = true;

        Logger.debug("handleSendSkills Cargado! - FALTA TERMINAR!");
    }
}
