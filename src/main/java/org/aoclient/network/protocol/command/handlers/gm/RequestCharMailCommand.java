package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharMail;
import static org.aoclient.network.protocol.command.GameCommand.LAST_EMAIL;

public class RequestCharMailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, LAST_EMAIL.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRequestCharMail(nick);
    }

}
