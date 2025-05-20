package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ChangeSpellSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(6)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int slot = tempBuffer.readByte();
        // tempBuffer.readByte();
        // short hechizoNum = tempBuffer.readInteger();
        tempBuffer.readInteger();
        String hechizoName = tempBuffer.readCp1252String();

        User.INSTANCE.getInventorySpells().addSpell(slot - 1, hechizoName);

        buffer.copy(tempBuffer);
        Logger.debug("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

}
