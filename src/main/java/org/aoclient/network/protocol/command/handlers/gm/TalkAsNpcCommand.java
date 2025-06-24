package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTalkAsNPC;

public class TalkAsNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/talkas <message>");
        requireValidString(context, "message", REGEX);
        String message = context.getArgumentsRaw().trim();
        writeTalkAsNPC(message);
    }

}
