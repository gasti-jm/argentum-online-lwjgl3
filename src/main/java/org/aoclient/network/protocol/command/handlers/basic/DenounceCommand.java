package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeDenounce;

public class DenounceCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeDenounce(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("File your denounce.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
