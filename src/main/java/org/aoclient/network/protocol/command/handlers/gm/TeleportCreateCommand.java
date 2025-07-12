package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.teleportCreate;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CREATE_TELEPORT;

public class TeleportCreateCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        // Si tiene menos de 3 argumentos o mas de 4
        if (commandContext.getArgumentCount() < 3 || commandContext.getArgumentCount() > 4)
            showError("Missing arguments. Usage: " + getCommandUsage(CREATE_TELEPORT));

        requireInteger(commandContext, 0, "map");
        requireInteger(commandContext, 1, "x");
        requireInteger(commandContext, 2, "y");

        short map = Short.parseShort(commandContext.getArgument(0));
        int x = Integer.parseInt(commandContext.getArgument(1));
        int y = Integer.parseInt(commandContext.getArgument(2));

        // Argumento opcional
        int radius = 0;
        if (commandContext.getArgumentCount() == 4) {
            requireInteger(commandContext, 3, "radius");
            radius = Integer.parseInt(commandContext.getArgument(3));
        }

        teleportCreate(map, x, y, radius);
    }

}
