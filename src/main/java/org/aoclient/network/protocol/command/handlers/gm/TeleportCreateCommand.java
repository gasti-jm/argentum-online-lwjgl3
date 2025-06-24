package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTeleportCreate;

public class TeleportCreateCommand extends BaseCommandHandler {

    private static final String USAGE = "/ct <map> <x> <y> [radius]";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, USAGE);

        // Valida que tenga 3 o 4 argumentos
        if (context.getArgumentCount() < 3 || context.getArgumentCount() > 4) showError("Missing arguments. Usage: " + USAGE);

        requireInteger(context, 0, "map");
        short map = Short.parseShort(context.getArgument(0));

        requireInteger(context, 1, "x");
        int x = Integer.parseInt(context.getArgument(1));

        requireInteger(context, 2, "y");
        int y = Integer.parseInt(context.getArgument(2));

        // Maneja argumento opcional radius
        int radius = 0; // valor por defecto
        if (context.getArgumentCount() == 4) {
            requireInteger(context, 3, "radius");
            radius = Integer.parseInt(context.getArgument(3));
        }

        writeTeleportCreate(map, x, y, radius);
    }

}
