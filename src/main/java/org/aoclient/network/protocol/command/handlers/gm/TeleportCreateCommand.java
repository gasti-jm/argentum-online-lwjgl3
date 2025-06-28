package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTeleportCreate;

@Command("/ct")
@SuppressWarnings("unused")
public class TeleportCreateCommand extends BaseCommandHandler {

    private static final String USAGE = "/ct <map> <x> <y> [radius]";

    @Override
    public void handle(CommandContext context) throws CommandException {
        // Si tiene menos de 3 argumentos o mas de 4
        if (context.getArgumentCount() < 3 || context.getArgumentCount() > 4) showError("Missing arguments. Usage: " + USAGE);

        requireInteger(context, 0, "map");
        requireInteger(context, 1, "x");
        requireInteger(context, 2, "y");

        short map = Short.parseShort(context.getArgument(0));
        int x = Integer.parseInt(context.getArgument(1));
        int y = Integer.parseInt(context.getArgument(2));

        // Argumento opcional
        int radius = 0;
        if (context.getArgumentCount() == 4) {
            requireInteger(context, 3, "radius");
            radius = Integer.parseInt(context.getArgument(3));
        }

        writeTeleportCreate(map, x, y, radius);
    }

}
