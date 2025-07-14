package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.nickToIp;
import static org.aoclient.network.protocol.command.metadata.GameCommand.NICK_TO_IP;

public class NickToIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(NICK_TO_IP));
        String nick = commandContext.getArgument(0);
        nickToIp(nick);
    }

}
