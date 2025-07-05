package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeServerMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.RMSG;

// TODO Raro que no se pase nada por parametro al metodo write()

public class ServerMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, RMSG.getCommand() + " <message>");
        requireValidString(commandContext, "message", REGEX);
        writeServerMessage();
    }

}
