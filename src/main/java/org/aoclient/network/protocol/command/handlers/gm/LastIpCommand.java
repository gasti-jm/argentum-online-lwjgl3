package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.lastIp;
import static org.aoclient.network.protocol.command.metadata.GameCommand.LAST_IP;

public class LastIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(LAST_IP));
        String player = commandContext.getArgument(0);
        lastIp(player);
    }

}
