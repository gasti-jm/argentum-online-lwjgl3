package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.requestCharGold;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REQUEST_CHAR_GOLD;

public class RequestCharGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REQUEST_CHAR_GOLD));
        String nick = commandContext.getArgument(0);
        requestCharGold(nick);
    }

}
