package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Reputation;
import org.aoclient.network.PacketBuffer;

public class FameHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(29)) return;
        buffer.readByte();

        int[] reputations = new int[Reputation.values().length];
        for (Reputation reputation : Reputation.values())
            reputations[reputation.ordinal()] = buffer.readLong();

        User.INSTANCE.setReputations(reputations);
        long average = buffer.readLong();
        User.INSTANCE.setCriminal(average < 0);
    }

}
