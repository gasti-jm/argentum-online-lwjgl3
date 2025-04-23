package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Reputation;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class FameHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {

        if (data.checkPacketData(29)) return;

        data.readByte();

        int[] reputations = new int[E_Reputation.values().length];
        for (E_Reputation reputation: E_Reputation.values()) {
            reputations[reputation.ordinal()] = data.readLong();
        }

        User.get().setReputations(reputations);
        long average = data.readLong();
        User.get().setIsCriminal(average < 0);
        // LlegoFama = True

        Logger.debug("handleFame Cargado! - FALTA TERMINAR!");
    }
}
