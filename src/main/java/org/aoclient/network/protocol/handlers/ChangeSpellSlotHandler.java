package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ChangeSpellSlotHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(6)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        //int slot = buffer.readByte();
        buffer.readByte();
        //short hechizoNum = buffer.readInteger();
        buffer.readInteger();
        String hechizoName = buffer.readASCIIString();

        User.get().getInventorySpells().addSpell(hechizoName);

        data.copyBuffer(buffer);
        Logger.debug("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

}
