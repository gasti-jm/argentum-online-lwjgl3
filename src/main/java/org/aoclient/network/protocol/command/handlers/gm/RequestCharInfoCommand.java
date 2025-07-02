package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharInfo;

@Command("/info")
@SuppressWarnings("unused")
public class RequestCharInfoCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/info <nick>");
        String nick = textContext.getArgument(0);
        writeRequestCharInfo(nick);
    }

}
