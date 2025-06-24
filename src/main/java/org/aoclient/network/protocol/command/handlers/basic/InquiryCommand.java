package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeInquiry;
import static org.aoclient.network.protocol.Protocol.writeInquiryVote;

/**
 * TODO Otro caso de inconsistencia en donde las opciones invalidas se manejan desde el servidor
 */

public class InquiryCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() == 0) writeInquiry();
        else {
            requireArguments(context, 1, "/encuesta <option>");
            requireInteger(context, 0, "option");
            writeInquiryVote(Integer.parseInt(context.getArgumentsRaw()));
        }
    }

}
