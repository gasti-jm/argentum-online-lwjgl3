package org.aoclient.network.protocol.command.handlers.pet;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeReleasePet;

public class ReleasePetCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writeReleasePet();
    }

}
