package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.unbanChar;
import static org.aoclient.network.protocol.command.metadata.GameCommand.UNBAN;

public class UnbanCharCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(UNBAN));
        String nick = commandContext.getArgument(0);
        unbanChar(nick);
    }

}
