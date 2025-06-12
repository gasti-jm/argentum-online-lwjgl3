package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeChatColor;

public class ChatColorCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 3) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(1), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(2), NumericType.BYTE)) {
                writeChatColor(Integer.parseInt(context.getArgument(0)), Integer.parseInt(context.getArgument(1)), Integer.parseInt(context.getArgument(2)));
            } else
                console.addMsgToConsole(new String("Incorrect value. Usage: /chatcolor <r> <g> <b>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else if (!context.hasArguments()) writeChatColor(0, 255, 0);
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /chatcolor <r> <g> <b>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
