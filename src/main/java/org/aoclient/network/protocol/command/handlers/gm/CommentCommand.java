package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeComment;

@Command("/rem")
@SuppressWarnings("unused")
public class CommentCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/rem <comment>");
        requireValidString(textContext, "comment", REGEX);
        String comment = textContext.argumentsRaw().trim();
        writeComment(comment);
    }

}
