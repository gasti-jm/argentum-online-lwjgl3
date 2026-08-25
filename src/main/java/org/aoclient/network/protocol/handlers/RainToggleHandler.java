package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.player.Player;
import org.aoclient.network.PacketBuffer;

public class RainToggleHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        int userX = Player.INSTANCE.getUserPos().getX();
        int userY = Player.INSTANCE.getUserPos().getY();
        if (Player.INSTANCE.inMapBounds(userX, userY)) return;

        Player.INSTANCE.setUnderCeiling(Player.INSTANCE.checkUnderCeiling());

        if (Rain.INSTANCE.isRaining()) {
            Rain.INSTANCE.setRainValue(false);
            Rain.INSTANCE.stopRainingSoundLoop();
            Rain.INSTANCE.playEndRainSound();
        } else Rain.INSTANCE.setRainValue(true);

    }

}
