package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class AddSlotsHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        int maxInventorySlots = data.readByte();
    }

}
