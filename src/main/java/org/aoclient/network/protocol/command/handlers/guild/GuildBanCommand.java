package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildBan;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_BAN;

public class GuildBanCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_BAN.getCommand() + " <name>");
        String name = commandContext.getArgument(0);
        writeGuildBan(name);
    }

}
