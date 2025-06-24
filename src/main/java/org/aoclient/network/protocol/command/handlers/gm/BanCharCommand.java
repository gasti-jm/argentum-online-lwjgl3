package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBanChar;

public class BanCharCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/ban <nick> <reason>");
        requireString(context, 0, "nick");
        requireString(context, 1, "reason");

        String nick = context.getArgument(0);
        String reason = context.getArgument(1);

        writeBanChar(nick, reason);

    }

}
