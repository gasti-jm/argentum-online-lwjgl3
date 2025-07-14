package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.guildOnlineSpecific;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_ONLINE_SPECIFIC;

public class GuildOnlineSpecificCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(GUILD_ONLINE_SPECIFIC));
        String guild = commandContext.getArgument(0);
        guildOnlineSpecific(guild);
    }

}
