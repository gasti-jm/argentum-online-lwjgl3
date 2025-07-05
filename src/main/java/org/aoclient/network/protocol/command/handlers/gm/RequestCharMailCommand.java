package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.requestCharMail;
import static org.aoclient.network.protocol.command.metadata.GameCommand.LAST_EMAIL;

public class RequestCharMailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, LAST_EMAIL.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        requestCharMail(nick);
    }

}
