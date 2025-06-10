package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeGuildOnline;

public class GuildOnlineCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writeGuildOnline();
    }

}
