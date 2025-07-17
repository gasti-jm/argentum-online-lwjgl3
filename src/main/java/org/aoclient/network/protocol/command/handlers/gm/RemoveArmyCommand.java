package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.removeArmy;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REMOVE_ARMY;

public class RemoveArmyCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REMOVE_ARMY));
        String player = commandContext.getArgument(0);
        removeArmy(player);
    }

}
