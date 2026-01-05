package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.engine.renderer.TextureManager;
import org.aoclient.network.PacketBuffer;

public class ChangeBankSlotHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(21)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int slot = tempBuffer.readByte();

        short objIndex = tempBuffer.readInteger();
        String name = tempBuffer.readCp1252String();
        int amount = tempBuffer.readInteger();
        short grhIndex = tempBuffer.readInteger();
        int objType = tempBuffer.readByte();
        short maxHit = tempBuffer.readInteger();
        short minHit = tempBuffer.readInteger();
        short maxDef = tempBuffer.readInteger();
        short minDef = tempBuffer.readInteger();
        float value = tempBuffer.readFloat();

        FBank.invNPC.setItem(slot - 1, objIndex, amount, false, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);
        buffer.copy(tempBuffer);
    }

}
