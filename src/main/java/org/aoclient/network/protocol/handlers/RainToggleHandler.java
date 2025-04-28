package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class RainToggleHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        final int userX = User.get().getUserPos().getX();
        final int userY = User.get().getUserPos().getY();
        if (User.get().inMapBounds(userX, userY)) return;

        User.get().setUnderCeiling(User.get().checkUnderCeiling());

        if (Rain.get().isRaining()) {
            Rain.get().setRainValue(false);
            Rain.get().stopRainingSoundLoop();
            Rain.get().playEndRainSound();
        } else Rain.get().setRainValue(true);

    }

}
