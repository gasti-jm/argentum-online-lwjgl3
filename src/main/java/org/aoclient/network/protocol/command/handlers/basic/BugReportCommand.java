package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBugReport;
import static org.aoclient.network.protocol.command.GameCommand.BUG;

public class BugReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, BUG.getCommand() + " <description>");
        requireValidString(commandContext, "description", REGEX);
        String description = commandContext.argumentsRaw().trim();
        writeBugReport(description);
    }

}
