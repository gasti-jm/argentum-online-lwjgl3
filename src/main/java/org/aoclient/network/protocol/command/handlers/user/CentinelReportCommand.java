package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.centinelReport;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CENTINEL_CODE;

public class CentinelReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, CENTINEL_CODE.getCommand() + " <code>");
        requireInteger(commandContext, 0, "code");
        int code = Integer.parseInt(commandContext.getArgument(0));
        centinelReport(code);
    }

}
