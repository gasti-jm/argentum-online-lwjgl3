package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.requestCharStats;
import static org.aoclient.network.protocol.command.metadata.GameCommand.STAT;

public class RequestCharStatsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(STAT));
        String nick = commandContext.getArgument(0);
        requestCharStats(nick);
    }

}
