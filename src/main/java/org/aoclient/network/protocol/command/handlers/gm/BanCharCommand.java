package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBanChar;

@Command("/ban")
@SuppressWarnings("unused")
public class BanCharCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/ban <nick> <reason>");
        requireString(textContext, 0, "nick");
        requireString(textContext, 1, "reason");

        String nick = textContext.getArgument(0);
        String reason = textContext.getArgument(1);

        writeBanChar(nick, reason);
    }

}
