package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildOnlineMembers;
import static org.aoclient.network.protocol.command.GameCommand.GUILD_ONLINE_SPECIFIC;

public class GuildOnlineMembersCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_ONLINE_SPECIFIC.getCommand() + " <name>");
        String name = commandContext.getArgument(0);
        writeGuildOnlineMembers(name);
    }

}
