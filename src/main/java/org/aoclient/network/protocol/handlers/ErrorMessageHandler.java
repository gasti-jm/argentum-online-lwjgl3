package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FCreateCharacter;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.Connection;

public class ErrorMessageHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String errMsg = tempBuffer.readCp1252String();
        ImGUISystem.INSTANCE.show(new FMessage(errMsg));

        Connection.INSTANCE.disconnect();
        User.INSTANCE.setUserConected(false);

        // Si tenemos un error en el FCreateCharacter, permite volver enviar la peticion al servidor.
        FCreateCharacter.sendCreate = false;

        buffer.copy(tempBuffer);
    }

}
