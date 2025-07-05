package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.setCharDescription;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SETDESC;

public class SetCharDescriptionCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, SETDESC.getCommand() + " <description>");
        requireValidString(commandContext, "description", REGEX);
        String description = commandContext.argumentsRaw().trim();
        setCharDescription(description);
    }

}
