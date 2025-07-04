package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCriminalMessage;

public class CriminalMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, "/crimsg <message>");
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        writeCriminalMessage(message);
    }

}
