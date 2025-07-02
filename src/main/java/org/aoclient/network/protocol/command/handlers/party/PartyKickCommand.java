package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartyKick;

@Command("/echarparty")
@SuppressWarnings("unused")
public class PartyKickCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/echarparty <nick>");
        String nick = textContext.getArgument(0);
        writePartyKick(nick);
    }

}
