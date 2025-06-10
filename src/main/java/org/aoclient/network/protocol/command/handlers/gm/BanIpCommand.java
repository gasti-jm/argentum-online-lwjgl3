package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeBanIP;

public class BanIpCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() >= 2) {
            if (validator.isValidIPv4(context.getArgument(0)))
                writeBanIP(true, validator.str2ipv4l(context.getArgument(0)), "", context.getArgumentsRaw().substring(context.getArgument(0).length() + 1));
            else
                writeBanIP(false, validator.str2ipv4l("0.0.0.0"), context.getArgument(0), context.getArgumentsRaw().substring(context.getArgument(0).length() + 1));
        } else
            console.addMsgToConsole(new String("Missing parameters. Use \"/BANIP ip reason\" or \"/BANIP nick reason\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
