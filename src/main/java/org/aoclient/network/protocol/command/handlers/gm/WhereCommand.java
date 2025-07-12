package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.where;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW_LOCATION;

public class WhereCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(SHOW_LOCATION));
        String nick = commandContext.getArgument(0);
        where(nick);
    }

}
