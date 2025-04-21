package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class ChangeNPCInventorySlotHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(21)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        int slot = buffer.readByte();

        String name = buffer.readASCIIString();
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
        data.copyBuffer(buffer);
    }

}
