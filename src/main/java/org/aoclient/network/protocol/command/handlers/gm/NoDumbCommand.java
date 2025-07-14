package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.noDumb;
import static org.aoclient.network.protocol.command.metadata.GameCommand.NO_DUMB;

public class NoDumbCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(NO_DUMB));
        String player = commandContext.getArgument(0);
        noDumb(player);
    }

}
