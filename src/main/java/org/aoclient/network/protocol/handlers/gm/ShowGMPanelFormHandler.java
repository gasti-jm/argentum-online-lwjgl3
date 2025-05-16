package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FOptions;
import org.aoclient.engine.gui.forms.FPanelGM;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;

public class ShowGMPanelFormHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        ImGUISystem.get().show(new FPanelGM());
    }

}
