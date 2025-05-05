package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class BankEndHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        User.get().setUserComerciando(false);
    }
}
