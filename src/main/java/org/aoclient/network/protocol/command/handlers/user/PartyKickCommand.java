package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.partyKick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_KICK;

public class PartyKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PARTY_KICK));
        String player = commandContext.getArgument(0);
        partyKick(player);
    }

}
