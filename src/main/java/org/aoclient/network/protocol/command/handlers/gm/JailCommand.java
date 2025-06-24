package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeJail;

public class JailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, "/carcel <nick> <reason> <minutes>");
        requireString(context, 0, "nick");
        requireString(context, 1, "reason");
        requireInteger(context, 2, "minutes");

        String nick = context.getArgument(0);
        String reason = context.getArgument(1);
        int minutes = Integer.parseInt(context.getArgument(2));

        writeJail(nick, reason, minutes);

    }

}
