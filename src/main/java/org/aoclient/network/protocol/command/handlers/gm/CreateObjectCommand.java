package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.createObject;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CREATE_OBJ;

/**
 * Al crear el hacha (objeto #3 del obj.dat del servidor VB6), se indica GrhIndex=505 pero el grafico real es el 16037 en
 * graphics-descompressed del cliente Java.
 */

public class CreateObjectCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(CREATE_OBJ));
        requireInteger(commandContext, 0, "object_id");
        int objectId = Integer.parseInt(commandContext.getArgument(0));
        createObject(objectId);
    }

}
