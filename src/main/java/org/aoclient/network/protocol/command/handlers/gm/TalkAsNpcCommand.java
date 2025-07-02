package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeTalkAsNPC;

@Command("/talkas")
@SuppressWarnings("unused")
public class TalkAsNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/talkas <message>");
        requireValidString(textContext, "message", REGEX);
        String message = textContext.argumentsRaw().trim();
        writeTalkAsNPC(message);
    }

}
