package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.summonChar;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SUM;

public class SummonCharCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(SUM));
        String nick = commandContext.getArgument(0);
        summonChar(nick); // TODO Raro que no se pase nada
    }

}
