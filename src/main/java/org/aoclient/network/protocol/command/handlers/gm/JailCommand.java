package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeJail;

public class JailCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            String[] tmpArr = context.getArgumentsRaw().split(" ");
            if (tmpArr.length == 3) {
                if (validator.isValidNumber(tmpArr[2], NumericType.BYTE))
                    writeJail(tmpArr[0], tmpArr[1], Integer.parseInt(tmpArr[2]));
                else
                    console.addMsgToConsole(new String("Incorrect time. Usage: /carcel <nick> <reason> <minutes>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else
                console.addMsgToConsole(new String("Incorrect format. Usage: /carcel <nick> <reason> <minutes>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing arguments. Usage: /carcel <nick> <reason> <minutes>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
