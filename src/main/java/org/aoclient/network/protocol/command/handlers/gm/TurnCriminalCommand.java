package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.turnCriminal;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CONDEN;

public class TurnCriminalCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(CONDEN));
        String nick = commandContext.getArgument(0);
        turnCriminal(nick);
    }

}
