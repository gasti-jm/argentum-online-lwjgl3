package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildMessage;

public class GuildMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/cmsg <message>");
        requireValidString(context, "message", REGEX);
        String message = context.getArgumentsRaw().trim();
        writeGuildMessage(message);
    }

}
