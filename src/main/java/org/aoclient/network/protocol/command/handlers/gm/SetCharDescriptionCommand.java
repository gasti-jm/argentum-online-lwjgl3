package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeSetCharDescription;

public class SetCharDescriptionCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeSetCharDescription(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /setdesc <desc>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());

    }

}
