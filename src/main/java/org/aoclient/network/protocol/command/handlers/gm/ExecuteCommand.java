package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeExecute;
import static org.aoclient.network.protocol.command.GameCommand.EXECUTE;

public class ExecuteCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, EXECUTE.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeExecute(nick);
    }

}
