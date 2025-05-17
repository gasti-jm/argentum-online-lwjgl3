package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BankInitHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        // Remove packet ID
        data.readByte();

        int bankGold = data.readLong();
        User.get().setUserComerciando(true);
        ImGUISystem.get().show(new FBank(bankGold));
    }
}
