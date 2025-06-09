package org.aoclient.network.protocol.command.handlers.pet;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writePetFollow;

public class PetFollowCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writePetFollow();
    }

}
