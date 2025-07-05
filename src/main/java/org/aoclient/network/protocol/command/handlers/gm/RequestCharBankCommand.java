package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharBank;
import static org.aoclient.network.protocol.command.GameCommand.REQUEST_CHAR_BANK;

public class RequestCharBankCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, REQUEST_CHAR_BANK.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRequestCharBank(nick);
    }

}
