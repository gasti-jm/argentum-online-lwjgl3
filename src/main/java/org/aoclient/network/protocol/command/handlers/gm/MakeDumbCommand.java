package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeMakeDumb;

public class MakeDumbCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeMakeDumb(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /estupido <nick>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
