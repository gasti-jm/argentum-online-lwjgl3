package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeUnbanIP;

public class UnbanIpCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidIPv4(context.getArgumentsRaw())) writeUnbanIP(validator.str2ipv4l(context.getArgumentsRaw()));
            else
                console.addMsgToConsole(new String("Incorrect IP. Use \"/UNBAN ip\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing parameters. Use \"/UNBAN ip\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
