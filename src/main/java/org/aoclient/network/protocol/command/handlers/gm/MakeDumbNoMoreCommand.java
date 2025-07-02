package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeMakeDumbNoMore;

@Command("/noestupido")
@SuppressWarnings("unused")
public class MakeDumbNoMoreCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/noestupido <nick>");
        String nick = textContext.getArgument(0);
        writeMakeDumbNoMore(nick);
    }

}
