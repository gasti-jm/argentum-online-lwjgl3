package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.guildOnlineMembers;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_ONLINE_SPECIFIC;

public class GuildOnlineMembersCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_ONLINE_SPECIFIC.getCommand() + " <name>");
        String name = commandContext.getArgument(0);
        guildOnlineMembers(name);
    }

}
