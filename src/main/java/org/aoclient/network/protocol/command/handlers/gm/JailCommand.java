package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeJail;

@Command("/carcel")
@SuppressWarnings("unused")
public class JailCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 3, "/carcel <nick> <reason> <minutes>");
        requireString(textContext, 0, "nick");
        requireString(textContext, 1, "reason");
        requireInteger(textContext, 2, "minutes");

        String nick = textContext.getArgument(0);
        String reason = textContext.getArgument(1);
        int minutes = Integer.parseInt(textContext.getArgument(2));

        writeJail(nick, reason, minutes);

    }

}
