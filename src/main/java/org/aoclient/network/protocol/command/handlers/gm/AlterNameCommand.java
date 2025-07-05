package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.alterName;
import static org.aoclient.network.protocol.command.metadata.GameCommand.ANAME;

public class AlterNameCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, ANAME.getCommand() + " <origin> <dest>");
        requireString(commandContext, 0, "origin");
        requireString(commandContext, 1, "dest");

        String origin = commandContext.getArgument(0);
        String dest = commandContext.getArgument(1);

        alterName(origin, dest);

    }

}
