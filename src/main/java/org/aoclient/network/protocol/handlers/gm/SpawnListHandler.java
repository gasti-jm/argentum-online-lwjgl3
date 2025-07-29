package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FSpawnList;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class SpawnListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String creatureListString = tempBuffer.readCp1252String();

        // Dividimos por el carácter nulo
        String[] creatureList = creatureListString.split("\0");

        // Mostramos la lista en el GUI
        ImGUISystem.INSTANCE.show(new FSpawnList(creatureList));

        buffer.copy(tempBuffer);
    }

}
