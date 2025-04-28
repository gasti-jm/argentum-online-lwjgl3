package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.network.ByteQueue;

public class RemoveDialogsHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();
        Dialogs.removeAllDialogs();
    }

}
