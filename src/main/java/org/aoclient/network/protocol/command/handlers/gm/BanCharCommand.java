package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBanChar;

@Command("/ban")
@SuppressWarnings("unused")
public class BanCharCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/ban <nick> <reason>");
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "reason");

        String nick = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);

        writeBanChar(nick, reason);
    }

}
