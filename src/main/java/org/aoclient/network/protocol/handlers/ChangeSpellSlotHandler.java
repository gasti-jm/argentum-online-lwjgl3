package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;

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

        Player.INSTANCE.getInventorySpells().addSpell(slot - 1, hechizoName);

        buffer.copy(tempBuffer);
        Log.debug("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

}
