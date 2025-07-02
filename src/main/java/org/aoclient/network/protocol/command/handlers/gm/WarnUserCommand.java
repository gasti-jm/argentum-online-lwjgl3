package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeWarnUser;

@Command("/advertencia")
@SuppressWarnings("unused")
public class WarnUserCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/advertencia <nick> <reason>");
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "reason");
        String nick = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);
        writeWarnUser(nick, reason);
    }

}
