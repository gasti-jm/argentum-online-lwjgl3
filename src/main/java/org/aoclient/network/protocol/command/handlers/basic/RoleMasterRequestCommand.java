package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRoleMasterRequest;

public class RoleMasterRequestCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, "/rol <message>");
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        writeRoleMasterRequest(message);
    }

}
