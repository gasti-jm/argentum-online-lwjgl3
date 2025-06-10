package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeInquiry;
import static org.aoclient.network.protocol.Protocol.writeInquiryVote;

public class InquiryCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() == 0) writeInquiry();
        else {
            if (validator.isValidNumber(context.getArgumentsRaw(), NumericType.BYTE)) writeInquiryVote(Integer.parseInt(context.getArgumentsRaw()));
            else {
                console.addMsgToConsole(new String("To vote for an option, write /encuesta OPTIONNUMBER, for example to vote for option 1, write /encuesta 1.".getBytes(), StandardCharsets.UTF_8),
                        false, true, new RGBColor());
            }
        }
    }

}
