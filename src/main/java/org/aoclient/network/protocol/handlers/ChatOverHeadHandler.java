package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;

import static org.aoclient.engine.game.Dialogs.charDialogSet;
import static org.aoclient.engine.utils.GameData.charList;

public class ChatOverHeadHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(8)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String chat = buffer.readASCIIString();
        short charIndex = buffer.readInteger();
        int r = buffer.readByte();
        int g = buffer.readByte();
        int b = buffer.readByte();

        // es un NPC?
        if (charList[charIndex].getName().length() <= 1) Dialogs.removeDialogsNPCArea();

        charDialogSet(charIndex, chat, new RGBColor((float) r / 255, (float) g / 255, (float) b / 255));

        data.copyBuffer(buffer);

    }

}
