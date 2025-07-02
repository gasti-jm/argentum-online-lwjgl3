package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCitizenMessage;

@Command("/ciumsg")
@SuppressWarnings("unused")
public class CitizenMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/ciumsg <message>");
        requireValidString(textContext, "message", REGEX);
        String message = textContext.argumentsRaw().trim();
        writeCitizenMessage(message);
    }

}
