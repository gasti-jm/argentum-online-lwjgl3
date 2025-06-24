package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSystemMessage;

public class SystemMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/smsg <message>");
        requireValidString(context, "message", REGEX);
        String message = context.getArgumentsRaw().trim();
        writeSystemMessage(message);
    }

}
