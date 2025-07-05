package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.partyKick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_KICK;

public class PartyKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PARTY_KICK.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        partyKick(nick);
    }

}
