package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.removeChaos;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REMOVE_CHAOS;

public class RemoveChaosCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REMOVE_CHAOS));
        requireString(commandContext, 0, "player");
        String player = commandContext.getArgument(0);
        removeChaos(player);
    }

}
