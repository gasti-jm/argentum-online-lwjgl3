package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writePartyLeave;

public class PartyLeaveCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writePartyLeave();
    }

}
