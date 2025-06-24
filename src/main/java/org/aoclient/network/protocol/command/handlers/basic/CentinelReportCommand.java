package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCentinelReport;

public class CentinelReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/centinela <code>");
        requireInteger(context, 0, "code");
        int code = Integer.parseInt(context.getArgument(0));
        writeCentinelReport(code);
    }

}
