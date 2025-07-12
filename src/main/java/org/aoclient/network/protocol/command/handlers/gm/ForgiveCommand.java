package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.forgive;
import static org.aoclient.network.protocol.command.metadata.GameCommand.FORGIVE;

public class ForgiveCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(FORGIVE));
        String nick = commandContext.getArgument(0);
        forgive(nick);
    }

}
