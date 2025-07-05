package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeNickToIP;
import static org.aoclient.network.protocol.command.GameCommand.NICK2IP;

public class NickToIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, NICK2IP.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeNickToIP(nick);
    }

}
