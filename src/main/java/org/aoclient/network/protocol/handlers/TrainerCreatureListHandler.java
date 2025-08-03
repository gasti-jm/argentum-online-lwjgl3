package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FTrainer;
import org.aoclient.network.PacketBuffer;

public class TrainerCreatureListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String creatureListString = tempBuffer.readCp1252String();

        // Dividimos por el carácter nulo
        String[] creatureList = creatureListString.split("\0");

        User.INSTANCE.setUserBussy(true);

        // Mostramos la lista en el GUI
        ImGUISystem.INSTANCE.show(new FTrainer(creatureList));

        buffer.copy(tempBuffer);
    }

}
