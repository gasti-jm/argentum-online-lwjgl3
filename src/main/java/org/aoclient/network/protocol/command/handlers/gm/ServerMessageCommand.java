package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeServerMessage;

// TODO Raro que no se pase nada por parametro al metodo write()

@Command("/rmsg")
@SuppressWarnings("unused")
public class ServerMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, -1, "/rmsg <message>");
        requireValidString(textContext, "message", REGEX);
        writeServerMessage();
    }

}
