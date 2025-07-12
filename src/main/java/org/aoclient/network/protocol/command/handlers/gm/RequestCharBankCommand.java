package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.requestCharBank;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REQUEST_CHAR_BANK;

public class RequestCharBankCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REQUEST_CHAR_BANK));
        String nick = commandContext.getArgument(0);
        requestCharBank(nick);
    }

}
