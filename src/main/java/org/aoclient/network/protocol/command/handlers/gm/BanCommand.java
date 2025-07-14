package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.ban;
import static org.aoclient.network.protocol.command.metadata.GameCommand.BAN;

public class BanCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, getCommandUsage(BAN));
        requireString(commandContext, 0, "player");
        requireString(commandContext, 1, "reason");

        String player = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);

        ban(player, reason);
    }

}
