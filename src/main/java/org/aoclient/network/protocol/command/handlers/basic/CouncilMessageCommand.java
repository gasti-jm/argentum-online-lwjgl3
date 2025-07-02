package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCouncilMessage;

@Command("/bmsg")
@SuppressWarnings("unused")
public class CouncilMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/bmsg <message>");
        requireValidString(textContext, "message", REGEX);
        String message = textContext.argumentsRaw().trim();
        writeCouncilMessage(message);
    }

}
