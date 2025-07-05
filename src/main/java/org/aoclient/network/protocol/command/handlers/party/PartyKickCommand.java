package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartyKick;
import static org.aoclient.network.protocol.command.GameCommand.PARTY_KICK;

public class PartyKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PARTY_KICK.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writePartyKick(nick);
    }

}
