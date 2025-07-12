package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.criminalMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CRIMSG;

public class CriminalMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, getCommandUsage(CRIMSG));
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        criminalMessage(message);
    }

}
