package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeCreateNPC;

public class CreateNpcCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.INTEGER)) writeCreateNPC(Short.parseShort(context.getArgument(0)));
            else
                console.addMsgToConsole(new String("Incorrect npc. Use \"/ACC npc\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing parameters. Use \"/ACC npc\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
