package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeReloadObjects;

public class ReloadObjectsCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writeReloadObjects();
    }

}
