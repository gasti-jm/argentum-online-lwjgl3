package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.inquiry;
import static org.aoclient.network.protocol.Protocol.poll;
import static org.aoclient.network.protocol.command.metadata.GameCommand.POLL;

/**
 * TODO Otro caso de inconsistencia en donde las opciones invalidas se manejan desde el servidor
 */

public class PollCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (!commandContext.hasArguments()) inquiry();
        else {
            requireArguments(commandContext, 1, getCommandUsage(POLL));
            requireInteger(commandContext, 0, "option");
            int option = Integer.parseInt(commandContext.getArgument(0));
            poll(option);
        }
    }

}
