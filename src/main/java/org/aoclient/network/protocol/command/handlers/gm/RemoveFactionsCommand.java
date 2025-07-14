package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.removeFactions;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REMOVE_FACTIONS;

public class RemoveFactionsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REMOVE_FACTIONS));
        String player = commandContext.getArgument(0);
        removeFactions(player);
    }

}
