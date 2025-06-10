package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeCentinelReport;

public class CentinelReportCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() != 1)
            console.addMsgToConsole(new String("The /CENTINELA command requires an argument. Please enter the verification code.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());
        else {
            if (validator.isValidNumber(context.getArgumentsRaw(), NumericType.INTEGER))
                writeCentinelReport(Integer.parseInt(context.getArgumentsRaw()));
            else
                console.addMsgToConsole(new String("The verification code must be numeric. Use /CENTINELA X, where X is the verification code.".getBytes(), StandardCharsets.UTF_8),
                        false, true, new RGBColor());
        }
    }

}
