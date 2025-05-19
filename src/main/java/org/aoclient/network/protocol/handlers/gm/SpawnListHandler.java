package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FSpawnList;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class SpawnListHandler implements PacketHandler  {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String creatureListString = buffer.readCp1252String();
  
        // Dividimos por el carácter nulo
        String[] creatureList = creatureListString.split("\0");

        // Mostramos la lista en el GUI
        ImGUISystem.INSTANCE.show(new FSpawnList(creatureList));

        data.copy(buffer);
        Logger.debug("handleSpawnList Cargado! - FALTA TERMINAR!");
    }
}
