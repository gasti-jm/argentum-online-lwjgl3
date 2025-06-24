package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetCharDescription;

public class SetCharDescriptionCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/setdesc <desc>");
        requireValidString(context, "desc", REGEX);
        String desc = context.getArgumentsRaw();
        writeSetCharDescription(desc);
    }

}
