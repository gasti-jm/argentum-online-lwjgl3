package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;

public class RemovePunishmentCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            String[] tmpArr = context.getArgumentsRaw().split(" ", 3);
            if (tmpArr.length == 3) writeRemovePunishment(tmpArr[0], Integer.parseInt(tmpArr[1]), tmpArr[2]);
            else
                console.addMsgToConsole(new String("Missing arguments. Usage: /borrarpena <nick> <minutes> <newPunishment>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing arguments. Usage: /borrarpena <nick> <minutes> <newPunishment>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
