package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeNickToIP;
import static org.aoclient.network.protocol.command.metadata.GameCommand.NICK2IP;

public class NickToIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, NICK2IP.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeNickToIP(nick);
    }

}
