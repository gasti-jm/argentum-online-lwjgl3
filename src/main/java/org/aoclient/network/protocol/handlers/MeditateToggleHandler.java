package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class MeditateToggleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        org.aoclient.engine.game.User.INSTANCE.setMeditating(!org.aoclient.engine.game.User.INSTANCE.isMeditating());
        Logger.debug("handleMeditateToggle: Meditación cambiada a " + org.aoclient.engine.game.User.INSTANCE.isMeditating());
    }

}
