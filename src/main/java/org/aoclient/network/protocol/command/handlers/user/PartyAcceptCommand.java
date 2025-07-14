package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.partyAccept;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_ACCEPT;

public class PartyAcceptCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PARTY_ACCEPT));
        String player = commandContext.getArgument(0);
        partyAccept(player);
    }

}
