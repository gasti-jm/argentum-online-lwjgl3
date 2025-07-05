package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeMakeDumbNoMore;
import static org.aoclient.network.protocol.command.GameCommand.NO_STUPID;

public class MakeDumbNoMoreCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, NO_STUPID.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeMakeDumbNoMore(nick);
    }

}
