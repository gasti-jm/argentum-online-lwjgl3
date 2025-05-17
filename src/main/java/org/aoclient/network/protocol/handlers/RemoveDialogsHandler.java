package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.network.PacketBuffer;

public class RemoveDialogsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();
        Dialogs.removeAllDialogs();
    }

}
