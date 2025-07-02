package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCentinelReport;

@Command("/centinela")
@SuppressWarnings("unused")
public class CentinelReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/centinela <code>");
        requireInteger(commandContext, 0, "code");
        int code = Integer.parseInt(commandContext.getArgument(0));
        writeCentinelReport(code);
    }

}
