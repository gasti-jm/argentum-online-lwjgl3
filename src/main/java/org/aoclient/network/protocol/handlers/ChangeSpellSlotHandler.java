package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ChangeSpellSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(6)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        int slot = buffer.readByte();
        //buffer.readByte();
        //short hechizoNum = buffer.readInteger();
        buffer.readInteger();
        String hechizoName = buffer.readUTF8String();

        User.get().getInventorySpells().addSpell(slot - 1, hechizoName);

        data.copy(buffer);
        Logger.debug("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

}
