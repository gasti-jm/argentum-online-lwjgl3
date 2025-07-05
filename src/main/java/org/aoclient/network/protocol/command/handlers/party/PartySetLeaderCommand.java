package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartySetLeader;
import static org.aoclient.network.protocol.command.GameCommand.PARTY_SET_LEADER;

public class PartySetLeaderCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PARTY_SET_LEADER.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writePartySetLeader(nick);
    }

}
