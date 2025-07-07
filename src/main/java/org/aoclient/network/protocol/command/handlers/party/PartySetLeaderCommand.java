package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.partySetLeader;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_SET_LEADER;

public class PartySetLeaderCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PARTY_SET_LEADER.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        partySetLeader(nick);
    }

}
