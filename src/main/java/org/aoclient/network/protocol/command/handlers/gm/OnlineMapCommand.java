package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeOnlineMap;

public class OnlineMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            requireInteger(context, 0, "map");
            short mapNumber = Short.parseShort(context.getArgument(0));
            writeOnlineMap(mapNumber);
        } else writeOnlineMap(user.getUserMap()); // Si no se proporciona argumento, usar el mapa actual del usuario
    }

}
