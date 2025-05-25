package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.PacketBuffer;

public class ShowMessageBoxHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String msg = tempBuffer.readCp1252String();
        ImGUISystem.INSTANCE.show(new FMessage(msg));

        buffer.copy(tempBuffer);
    }

}
