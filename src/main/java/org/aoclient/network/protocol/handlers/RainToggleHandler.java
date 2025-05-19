package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

public class RainToggleHandler implements PacketHandler {


    @Override
    public void handle(PacketBuffer data) {
        data.readByte();

        int userX = User.INSTANCE.getUserPos().getX();
        int userY = User.INSTANCE.getUserPos().getY();
        if (User.INSTANCE.inMapBounds(userX, userY)) return;

        User.INSTANCE.setUnderCeiling(User.INSTANCE.checkUnderCeiling());

        if (Rain.INSTANCE.isRaining()) {
            Rain.INSTANCE.setRainValue(false);
            Rain.INSTANCE.stopRainingSoundLoop();
            Rain.INSTANCE.playEndRainSound();
        } else Rain.INSTANCE.setRainValue(true);

    }

}
