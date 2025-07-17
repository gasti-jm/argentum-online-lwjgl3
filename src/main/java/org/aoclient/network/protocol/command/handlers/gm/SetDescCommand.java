package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.setDesc;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SET_DESC;

public class SetDescCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, getCommandUsage(SET_DESC));
        requireValidString(commandContext, "description", REGEX);
        String desc = commandContext.argumentsRaw().trim();
        setDesc(desc);
    }

}
