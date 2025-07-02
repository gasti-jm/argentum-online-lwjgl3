package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharBank;

@Command("/bov")
@SuppressWarnings("unused")
public class RequestCharBankCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/bov <nick>");
        String nick = textContext.getArgument(0);
        writeRequestCharBank(nick);
    }

}
