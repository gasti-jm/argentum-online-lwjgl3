package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeJail;
import static org.aoclient.network.protocol.command.GameCommand.JAIL;

public class JailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, JAIL.getCommand() + " <nick> <reason> <minutes>");
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "reason");
        requireInteger(commandContext, 2, "minutes");

        String nick = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);
        int minutes = Integer.parseInt(commandContext.getArgument(2));

        writeJail(nick, reason, minutes);

    }

}
