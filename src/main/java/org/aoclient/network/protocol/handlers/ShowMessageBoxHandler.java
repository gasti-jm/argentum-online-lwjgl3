package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.PacketBuffer;

public class ShowMessageBoxHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String msg = buffer.readCp1252String();
        ImGUISystem.INSTANCE.show(new FMessage(msg));

        data.copy(buffer);
    }

}
