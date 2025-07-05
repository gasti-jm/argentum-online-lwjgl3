package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeResetFactions;
import static org.aoclient.network.protocol.command.GameCommand.RAJAR;

public class ResetFactionsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, RAJAR.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeResetFactions(nick);
    }

}
