package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeIgnored;

public class IgnoredCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writeIgnored();
    }

}
