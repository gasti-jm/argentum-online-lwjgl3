package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;

public class NavigateToggleHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        User.get().setUserNavegando(!User.get().isUserNavegando());
    }

}
