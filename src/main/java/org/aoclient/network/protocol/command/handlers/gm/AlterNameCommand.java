package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterName;

public class AlterNameCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/aname <origin> <dest>");
        requireString(commandContext, 0, "origin");
        requireString(commandContext, 1, "dest");

        String origin = commandContext.getArgument(0);
        String dest = commandContext.getArgument(1);

        writeAlterName(origin, dest);

    }

}
