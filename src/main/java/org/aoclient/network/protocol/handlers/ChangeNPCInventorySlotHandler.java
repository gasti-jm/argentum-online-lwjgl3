package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.PacketBuffer;

public class ChangeNPCInventorySlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(21)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int slot = tempBuffer.readByte();

        String name = tempBuffer.readCp1252String();
        short amount = tempBuffer.readInteger();
        float value = tempBuffer.readFloat();
        short grhIndex = tempBuffer.readInteger();
        short objIndex = tempBuffer.readInteger();
        int objType = tempBuffer.readByte();
        short maxHit = tempBuffer.readInteger();
        short minHit = tempBuffer.readInteger();
        short maxDef = tempBuffer.readInteger();
        short minDef = tempBuffer.readInteger();

        FComerce.invNPC.setItem(slot - 1, objIndex, amount, false, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);
        buffer.copy(tempBuffer);
    }

}
