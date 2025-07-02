package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTurnCriminal;

@Command("/conden")
@SuppressWarnings("unused")
public class TurnCriminalCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/conden <nick>");
        String nick = textContext.getArgument(0);
        writeTurnCriminal(nick);
    }

}
