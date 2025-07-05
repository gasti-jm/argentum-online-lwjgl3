package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeInquiry;
import static org.aoclient.network.protocol.Protocol.writeInquiryVote;
import static org.aoclient.network.protocol.command.metadata.GameCommand.POLL;

/**
 * TODO Otro caso de inconsistencia en donde las opciones invalidas se manejan desde el servidor
 */

public class InquiryCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (!commandContext.hasArguments()) writeInquiry();
        else {
            requireArguments(commandContext, 1, POLL.getCommand() + " <option>");
            requireInteger(commandContext, 0, "option");
            int option = Integer.parseInt(commandContext.getArgument(0));
            writeInquiryVote(option);
        }
    }

}
