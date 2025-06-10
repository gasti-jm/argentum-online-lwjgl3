package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeWarnUser;

public class WarnUserCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            String[] tmpArr = context.getArgumentsRaw().split("@", 2);
            if (tmpArr.length == 2) writeWarnUser(tmpArr[0], tmpArr[1]);
            else
                console.addMsgToConsole(new String("Incorrect format. Use \"/ADVERTENCIA nickname@reason\"".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing parameters. Use \"/ADVERTENCIA nickname@reason@time\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
