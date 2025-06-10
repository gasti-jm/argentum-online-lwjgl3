package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeBugReport;

public class BugReportCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeBugReport(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Write a description of the bug.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
