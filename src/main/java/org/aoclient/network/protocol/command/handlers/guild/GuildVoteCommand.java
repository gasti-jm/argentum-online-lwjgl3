package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildVote;

@Command("/voto")
@SuppressWarnings("unused")
public class GuildVoteCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/voto <nick>");
        String nick = textContext.getArgument(0);
        writeGuildVote(nick);
    }

}
