package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.PacketBuffer;

public class ChangeNPCInventorySlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(21)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        int slot = buffer.readByte();

        String name = buffer.readCp1252String();
        short amount = buffer.readInteger();
        float value = buffer.readFloat();
        short grhIndex = buffer.readInteger();
        short objIndex = buffer.readInteger();
        int objType = buffer.readByte();
        short maxHit = buffer.readInteger();
        short minHit = buffer.readInteger();
        short maxDef = buffer.readInteger();
        short minDef = buffer.readInteger();

        FComerce.invNPC.setItem(slot - 1, objIndex, amount, false, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);
        data.copy(buffer);
    }

}
