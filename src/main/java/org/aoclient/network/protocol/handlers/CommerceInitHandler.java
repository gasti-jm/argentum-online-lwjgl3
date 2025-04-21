package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class CommerceInitHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        // comerciando = true
        User.get().setUserComerciando(true);
        ImGUISystem.get().show(new FComerce());
    }
}
