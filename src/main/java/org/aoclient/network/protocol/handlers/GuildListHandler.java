package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FGuildAdm;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class GuildListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String guildNames = tempBuffer.readCp1252String();
        
        // Split the guild names using null character as separator
        String[] guildsArray = guildNames.split("\0");
        
        // Create and show the guild admin form with the guild list
        FGuildAdm guildAdm = new FGuildAdm();
        guildAdm.setGuildsList(guildsArray);
        ImGUISystem.INSTANCE.show(guildAdm);

        buffer.copy(tempBuffer);
        Logger.debug("Guild list loaded with " + guildsArray.length + " guilds");
    }
}
