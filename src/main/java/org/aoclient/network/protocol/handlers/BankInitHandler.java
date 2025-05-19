package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class BankInitHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        int bankGold = data.readLong();
        User.INSTANCE.setUserComerciando(true);
        ImGUISystem.INSTANCE.show(new FBank(bankGold));
    }

}
