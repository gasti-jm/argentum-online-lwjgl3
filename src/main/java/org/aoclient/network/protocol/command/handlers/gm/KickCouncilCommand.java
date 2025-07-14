package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.kickCouncil;
import static org.aoclient.network.protocol.command.metadata.GameCommand.KICK_COUNCIL;

public class KickCouncilCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(KICK_COUNCIL));
        String player = commandContext.getArgument(0);
        kickCouncil(player);
    }

}
