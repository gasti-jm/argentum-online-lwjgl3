package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class RemoveCharDialogHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;
        data.readByte();
        Dialogs.removeDialog(data.readInteger());
    }

}
