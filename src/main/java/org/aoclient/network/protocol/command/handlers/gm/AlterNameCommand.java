package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterName;

public class AlterNameCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/aname <origin> <dest>");
        requireString(context, 0, "origin");
        requireString(context, 1, "dest");

        String origin = context.getArgument(0);
        String dest = context.getArgument(1);

        writeAlterName(origin, dest);

    }

}
