package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.acceptChaosCouncil;
import static org.aoclient.network.protocol.command.metadata.GameCommand.ACCEPT_CHAOS_COUNCIL;

public class AcceptChaosCouncilCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(ACCEPT_CHAOS_COUNCIL));
        String player = commandContext.getArgument(0);
        acceptChaosCouncil(player);
    }

}
