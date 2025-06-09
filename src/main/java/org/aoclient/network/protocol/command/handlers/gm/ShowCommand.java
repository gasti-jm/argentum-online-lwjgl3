package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import static org.aoclient.network.protocol.Protocol.writeSOSShowList;
import static org.aoclient.network.protocol.Protocol.writeShowServerForm;

public class ShowCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            switch (context.getArgument(0).toUpperCase()) {
                case "SOS":
                    writeSOSShowList();
                    break;
                case "INT":
                    writeShowServerForm();
                    break;
            }
        }
    }

}
