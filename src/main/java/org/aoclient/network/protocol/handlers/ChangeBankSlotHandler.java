package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;

public class ChangeBankSlotHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(21)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        final int slot = buffer.readByte();

        final short objIndex = buffer.readInteger();
        final String name = buffer.readASCIIString();
        final int amount = buffer.readInteger();
        final short grhIndex = buffer.readInteger();
        final int objType = buffer.readByte();
        final short maxHit = buffer.readInteger();
        final short minHit = buffer.readInteger();
        final short maxDef = buffer.readInteger();
        final short minDef = buffer.readInteger();
        final float value = buffer.readFloat();

        data.copyBuffer(buffer);
    }
}
