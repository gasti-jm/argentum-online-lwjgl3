package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCitizenMessage;

public class CitizenMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/ciumsg <message>");
        requireValidString(context, "message", REGEX);
        String message = context.getArgumentsRaw().trim();
        writeCitizenMessage(message);
    }

}
