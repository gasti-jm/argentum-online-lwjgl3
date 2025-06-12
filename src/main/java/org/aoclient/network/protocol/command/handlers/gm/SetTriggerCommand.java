package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeAskTrigger;
import static org.aoclient.network.protocol.Protocol.writeSetTrigger;

public class SetTriggerCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidNumber(context.getArgumentsRaw(), NumericType.INTEGER))
                writeSetTrigger(Integer.parseInt(context.getArgumentsRaw()));
            else
                console.addMsgToConsole(new String("Missing arguments. Usage: /trigger <number>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else writeAskTrigger(); // Version sin parametro
    }

}
