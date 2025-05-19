package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.SocketConnection;

public class ErrorMessageHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        String errMsg = buffer.readCp1252String();
        ImGUISystem.INSTANCE.show(new FMessage(errMsg));

        SocketConnection.INSTANCE.disconnect();
        User.INSTANCE.setUserConected(false);

        data.copy(buffer);
    }
    
}
