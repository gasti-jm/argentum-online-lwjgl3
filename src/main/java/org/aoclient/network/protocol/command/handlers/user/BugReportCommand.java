package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.bugReport;
import static org.aoclient.network.protocol.command.metadata.GameCommand.BUG;

public class BugReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, getCommandUsage(BUG));
        requireValidString(commandContext, "description", REGEX);
        String description = commandContext.argumentsRaw().trim();
        bugReport(description);
    }

}
