package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeTeleportCreate;

public class TeleportCreateCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 3) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.INTEGER) &&
                    validator.isValidNumber(context.getArgument(1), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(2), NumericType.BYTE)) {
                if (context.getArgumentCount() == 3)
                    writeTeleportCreate(Short.parseShort(context.getArgument(0)), Integer.parseInt(context.getArgument(1)), Integer.parseInt(context.getArgument(2)), 0);
                else {
                    if (validator.isValidNumber(context.getArgument(3), NumericType.BYTE))
                        writeTeleportCreate(Short.parseShort(context.getArgument(0)), Integer.parseInt(context.getArgument(1)), Integer.parseInt(context.getArgument(2)), Integer.parseInt(context.getArgument(3)));
                    else
                        console.addMsgToConsole(new String("Invalid arguments. Usage: /ct <map> <x> <y> [radius]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
                }
            } else
                console.addMsgToConsole(new String("Invalid arguments. Usage: /ct <map> <x> <y> [radius]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing arguments. Usage: /ct <map> <x> <y> [radius]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
