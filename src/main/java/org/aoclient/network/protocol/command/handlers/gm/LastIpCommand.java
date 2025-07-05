package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeLastIP;
import static org.aoclient.network.protocol.command.GameCommand.LAST_IP;

public class LastIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, LAST_IP.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeLastIP(nick);
    }

}
