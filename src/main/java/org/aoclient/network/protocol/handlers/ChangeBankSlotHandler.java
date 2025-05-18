package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.PacketBuffer;

public class ChangeBankSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(21)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        final int slot = buffer.readByte();

        final short objIndex = buffer.readInteger();
        final String name = buffer.readCp1252String();
        final int amount = buffer.readInteger();
        final short grhIndex = buffer.readInteger();
        final int objType = buffer.readByte();
        final short maxHit = buffer.readInteger();
        final short minHit = buffer.readInteger();
        final short maxDef = buffer.readInteger();
        final short minDef = buffer.readInteger();
        final float value = buffer.readFloat();

        FBank.invNPC.setItem(slot - 1, objIndex, amount, false, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);
        data.copy(buffer);
    }
}
