package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeReviveChar;

public class ReviveCharCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeReviveChar(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /revivir <nick>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());

    }

}
