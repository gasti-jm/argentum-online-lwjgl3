package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class AddSlotsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        int maxInventorySlots = data.readByte();
    }

}
