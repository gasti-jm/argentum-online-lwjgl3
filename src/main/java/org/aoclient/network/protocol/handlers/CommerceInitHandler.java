package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.network.PacketBuffer;

public class CommerceInitHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        // comerciando = true
        Player.INSTANCE.setUserComerciando(true);
        ImGUISystem.INSTANCE.show(new FComerce());
    }

}
