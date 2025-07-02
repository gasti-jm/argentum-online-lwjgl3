package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeDenounce;

@Command("/denunciar")
@SuppressWarnings("unused")
public class DenounceCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/denunciar <message>");
        requireValidString(textContext, "message", REGEX);
        String message = textContext.argumentsRaw().trim();
        writeDenounce(message);
    }

}
