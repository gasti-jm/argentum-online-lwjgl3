package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeComment;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REM;

public class CommentCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, REM.getCommand() + " <comment>");
        requireValidString(commandContext, "comment", REGEX);
        String comment = commandContext.argumentsRaw().trim();
        writeComment(comment);
    }

}
