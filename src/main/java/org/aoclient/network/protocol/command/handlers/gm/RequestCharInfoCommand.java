package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharInfo;
import static org.aoclient.network.protocol.command.GameCommand.PLAYER_INFO;

public class RequestCharInfoCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PLAYER_INFO.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRequestCharInfo(nick);
    }

}
