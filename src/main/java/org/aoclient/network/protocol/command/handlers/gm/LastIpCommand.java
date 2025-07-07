package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.lastIP;
import static org.aoclient.network.protocol.command.metadata.GameCommand.LAST_IP;

public class LastIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, LAST_IP.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        lastIP(nick);
    }

}
