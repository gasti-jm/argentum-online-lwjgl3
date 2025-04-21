package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class ShowMessageBoxHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String msg = buffer.readASCIIString();
        ImGUISystem.get().show(new FMessage(msg));

        data.copyBuffer(buffer);
    }

}
