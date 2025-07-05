package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildVote;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_VOTE;

public class GuildVoteCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_VOTE.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeGuildVote(nick);
    }

}
