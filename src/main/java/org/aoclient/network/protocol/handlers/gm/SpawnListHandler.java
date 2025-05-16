package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FSpawnList;
import org.aoclient.engine.gui.forms.FTutorial;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class SpawnListHandler implements PacketHandler  {
    @Override
    public void handle(ByteQueue data) {
        // Si hay menos de 3 bytes disponibles, no seguimos
        if (data.checkPacketData(3)) return;

        // Clonamos el buffer original
        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        // Leemos la lista de criaturas como String ASCII separado por \0
        String creatureListString = buffer.readASCIIString();

        // Dividimos por el carácter nulo
        String[] creatureList = creatureListString.split("\0");

        // Mostramos la lista en el GUI
        ImGUISystem.get().show(new FSpawnList(creatureList));

        data.copyBuffer(buffer);
        Logger.debug("handleSpawnList Cargado! - FALTA TERMINAR!");
    }
}
