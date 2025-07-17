package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.map;

public class OnlineMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.hasArguments()) {
            requireInteger(commandContext, 0, "map");
            short mapNumber = Short.parseShort(commandContext.getArgument(0));
            map(mapNumber);
        } else map(user.getUserMap()); // Si no se proporciona argumento, usar el mapa actual del usuario
    }

}
