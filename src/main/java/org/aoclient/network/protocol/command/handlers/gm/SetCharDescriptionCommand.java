package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetCharDescription;

@Command("/setdesc")
@SuppressWarnings("unused")
public class SetCharDescriptionCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/setdesc <description>");
        requireValidString(textContext, "description", REGEX);
        String description = textContext.argumentsRaw().trim();
        writeSetCharDescription(description);
    }

}
