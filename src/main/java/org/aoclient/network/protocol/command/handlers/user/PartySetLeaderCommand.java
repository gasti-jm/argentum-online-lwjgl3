package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.partySetLeader;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_SET_LEADER;

public class PartySetLeaderCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PARTY_SET_LEADER));
        String nick = commandContext.getArgument(0);
        partySetLeader(nick);
    }

}
