package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeCreaturesInMap;

public class CreaturesInMapCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidNumber(context.getArgumentsRaw(), NumericType.INTEGER))
                writeCreaturesInMap(Short.parseShort(context.getArgumentsRaw()));
            else // No es numerico
                console.addMsgToConsole(new String("Wrong map. Usage: /nene <map>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else writeCreaturesInMap(user.getUserMap()); // Por defecto toma el mapa en el que esta
    }

}
