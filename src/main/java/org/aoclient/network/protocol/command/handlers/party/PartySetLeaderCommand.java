package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartySetLeader;

@Command("/partylider")
@SuppressWarnings("unused")
public class PartySetLeaderCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/partylider <nick>");
        String nick = textContext.getArgument(0);
        writePartySetLeader(nick);
    }

}
