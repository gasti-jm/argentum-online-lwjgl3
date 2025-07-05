package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetCharDescription;
import static org.aoclient.network.protocol.command.GameCommand.SETDESC;

public class SetCharDescriptionCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, SETDESC.getCommand() + " <description>");
        requireValidString(commandContext, "description", REGEX);
        String description = commandContext.argumentsRaw().trim();
        writeSetCharDescription(description);
    }

}
