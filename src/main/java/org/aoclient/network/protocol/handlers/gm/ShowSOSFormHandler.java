package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMSG;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class ShowSOSFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();
        String sosList = tempBuffer.readCp1252String();

        // Dividimos por el carácter nulo
        String[] MSGList = sosList.split("\0");

        // Mostramos la lista en el GUI
        ImGUISystem.INSTANCE.show(new FMSG(MSGList));

        buffer.copy(tempBuffer);
    }

}
