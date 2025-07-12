package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.guildVote;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_VOTE;

public class GuildVoteCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(GUILD_VOTE));
        String nick = commandContext.getArgument(0);
        guildVote(nick);
    }

}
