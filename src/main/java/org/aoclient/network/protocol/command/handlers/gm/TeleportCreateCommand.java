package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTeleportCreate;

@Command("/ct")
@SuppressWarnings("unused")
public class TeleportCreateCommand extends BaseCommandHandler {

    private static final String USAGE = "/ct <map> <x> <y> [radius]";

    @Override
    public void handle(TextContext textContext) throws CommandException {
        // Si tiene menos de 3 argumentos o mas de 4
        if (textContext.getArgumentCount() < 3 || textContext.getArgumentCount() > 4) showError("Missing arguments. Usage: " + USAGE);

        requireInteger(textContext, 0, "map");
        requireInteger(textContext, 1, "x");
        requireInteger(textContext, 2, "y");

        short map = Short.parseShort(textContext.getArgument(0));
        int x = Integer.parseInt(textContext.getArgument(1));
        int y = Integer.parseInt(textContext.getArgument(2));

        // Argumento opcional
        int radius = 0;
        if (textContext.getArgumentCount() == 4) {
            requireInteger(textContext, 3, "radius");
            radius = Integer.parseInt(textContext.getArgument(3));
        }

        writeTeleportCreate(map, x, y, radius);
    }

}
