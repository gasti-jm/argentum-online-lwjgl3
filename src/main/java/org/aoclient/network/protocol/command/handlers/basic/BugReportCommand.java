package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBugReport;

@Command("/bug")
@SuppressWarnings("unused")
public class BugReportCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/bug <description>");
        requireValidString(context, "description", REGEX);
        String description = context.argumentsRaw().trim();
        writeBugReport(description);
    }

}
