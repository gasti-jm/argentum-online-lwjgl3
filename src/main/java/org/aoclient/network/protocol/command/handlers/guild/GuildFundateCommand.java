package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeGuildFundate;

public class GuildFundateCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.getUserLvl() >= 25) writeGuildFundate();
        else
            console.addMsgToConsole(new String("To found a clan you must be level 25 and have 90 leadership skills.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());
    }

}
