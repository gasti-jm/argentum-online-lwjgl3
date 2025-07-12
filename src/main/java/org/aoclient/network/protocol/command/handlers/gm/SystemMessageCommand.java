package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.systemMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SMSG;

public class SystemMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, getCommandUsage(SMSG));
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        systemMessage(message);
    }

}
