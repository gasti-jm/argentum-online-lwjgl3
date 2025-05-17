package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.network.PacketBuffer;

public class RemoveCharDialogHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;
        data.readByte();
        Dialogs.removeDialog(data.readInteger());
    }

}
