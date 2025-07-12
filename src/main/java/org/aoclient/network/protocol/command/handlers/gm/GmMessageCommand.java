package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.GMMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GMSG;

public class GmMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, getCommandUsage(GMSG));
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        GMMessage(message);
    }

}
