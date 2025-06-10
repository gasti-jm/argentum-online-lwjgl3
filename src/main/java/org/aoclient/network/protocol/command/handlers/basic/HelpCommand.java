package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeHelp;

public class HelpCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        writeHelp();
    }

}
