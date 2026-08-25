package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.PacketBuffer;

public class BankInitHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        int bankGold = buffer.readLong();
        Player.INSTANCE.setUserComerciando(true);
        ImGUISystem.INSTANCE.show(new FBank(bankGold));
    }

}
