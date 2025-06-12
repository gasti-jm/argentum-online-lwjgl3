package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import static org.aoclient.network.protocol.Protocol.writeCreateItem;

/**
 * No se porque al crear el hacha que es el objeto numero 3 (especificado en la seccion [OBJ3] dentro del archivo obj.dat de la
 * carpeta del servidor en VB6) se indica que el grafico es el 505 (GrhIndex=505) cuando en realidad el grafico es el 16037 que se
 * especifica en la carpeta graphics-descompressed del cliente en Java.
 * <p>
 * TODO En realidad se esta creando un objeto, no un item
 */

public class CreateItemCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.LONG))
                writeCreateItem(Integer.parseInt(context.getArgument(0)));
            else console.addMsgToConsole("Incorrect object.", false, true, new RGBColor());
        } else console.addMsgToConsole("Missing arguments. Usage: /ci <object>", false, true, new RGBColor());
    }

}
