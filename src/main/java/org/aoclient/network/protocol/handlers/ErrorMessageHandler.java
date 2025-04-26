package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.SocketConnection;

public class ErrorMessageHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet ID
        buffer.readByte();

        String errMsg = buffer.readASCIIString();
        ImGUISystem.get().show(new FMessage(errMsg));

        SocketConnection.get().disconnect();
        User.get().setUserConected(false);

        data.copyBuffer(buffer);
    }
    
}
