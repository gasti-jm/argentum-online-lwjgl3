package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBugReport;

@Command("/bug")
@SuppressWarnings("unused")
public class BugReportCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/bug <description>");
        requireValidString(textContext, "description", REGEX);
        String description = textContext.argumentsRaw().trim();
        writeBugReport(description);
    }

}
