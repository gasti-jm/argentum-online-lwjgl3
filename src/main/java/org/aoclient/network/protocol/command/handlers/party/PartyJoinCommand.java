package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writePartyJoin;

public class PartyJoinCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writePartyJoin();
    }

}
