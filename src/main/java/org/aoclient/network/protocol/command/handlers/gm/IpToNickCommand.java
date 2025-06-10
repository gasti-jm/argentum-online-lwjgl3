package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeIPToNick;

public class IpToNickCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidIPv4(context.getArgumentsRaw())) {
                int[] ip = validator.str2ipv4l(context.getArgumentsRaw());
                if (ip != null) writeIPToNick(ip);
                else
                    console.addMsgToConsole(new String("Error converting IP.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else
                console.addMsgToConsole(new String("Incorrect IP. Use \"/IP2NICK ip\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing parameters. Use \"/IP2NICK ip\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
