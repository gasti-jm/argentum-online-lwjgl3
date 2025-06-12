package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeWhere;

public class WhereCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeWhere(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /donde <nick>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
