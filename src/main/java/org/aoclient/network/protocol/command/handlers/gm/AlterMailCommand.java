package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeAlterMail;

public class AlterMailCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            String[] tmpArr = validator.AEMAILSplit(context.getArgumentsRaw());
            if (tmpArr[0].isEmpty())
                console.addMsgToConsole(new String("Incorrect format. Usage: /aemail <nick>-<newmail>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            else writeAlterMail(tmpArr[0], tmpArr[1]);
        } else
            console.addMsgToConsole(new String("Missing arguments. Usage: /aemail <nick>-<newmail>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
