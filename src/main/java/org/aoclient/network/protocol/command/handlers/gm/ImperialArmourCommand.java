package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeImperialArmour;

public class ImperialArmourCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 2) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE) && validator.isValidNumber(context.getArgument(1), NumericType.INTEGER))
                writeImperialArmour(Integer.parseInt(context.getArgument(0)), Short.parseShort(context.getArgument(1)));
            else
                console.addMsgToConsole(new String("Incorrect parameters.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing arguments. Usage: /ai <armor> <object>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
