package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Reputation;
import org.aoclient.network.PacketBuffer;

public class FameHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {

        if (data.checkBytes(29)) return;

        data.readByte();

        int[] reputations = new int[E_Reputation.values().length];
        for (E_Reputation reputation: E_Reputation.values()) {
            reputations[reputation.ordinal()] = data.readLong();
        }

        User.INSTANCE.setReputations(reputations);
        long average = data.readLong();
        User.INSTANCE.setCriminal(average < 0);
    }
}
