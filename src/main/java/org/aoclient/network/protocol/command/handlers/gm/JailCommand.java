package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.jail;
import static org.aoclient.network.protocol.command.metadata.GameCommand.JAIL;

public class JailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, getCommandUsage(JAIL));
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "reason");
        requireInteger(commandContext, 2, "minutes");

        String nick = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);
        int minutes = Integer.parseInt(commandContext.getArgument(2));

        jail(nick, reason, minutes);

    }

}
