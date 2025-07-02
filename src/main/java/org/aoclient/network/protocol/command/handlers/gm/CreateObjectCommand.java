package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateObject;

/**
 * Al crear el hacha (objeto #3 del obj.dat del servidor VB6), se indica GrhIndex=505 pero el grafico real es el 16037 en
 * graphics-descompressed del cliente Java.
 */

@Command("/co")
@SuppressWarnings("unused")
public class CreateObjectCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/co <object_id>");
        requireInteger(textContext, 0, "object_id");
        int objectId = Integer.parseInt(textContext.getArgument(0));
        writeCreateObject(objectId);
    }

}
