package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeInquiry;
import static org.aoclient.network.protocol.Protocol.writeInquiryVote;

/**
 * TODO Otro caso de inconsistencia en donde las opciones invalidas se manejan desde el servidor
 */

@Command("/encuesta")
@SuppressWarnings("unused")
public class InquiryCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        if (!textContext.hasArguments()) writeInquiry();
        else {
            requireArguments(textContext, 1, "/encuesta <option>");
            requireInteger(textContext, 0, "option");
            int option = Integer.parseInt(textContext.getArgument(0));
            writeInquiryVote(option);
        }
    }

}
