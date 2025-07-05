package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeServerMessage;
import static org.aoclient.network.protocol.command.GameCommand.RMSG;

// TODO Raro que no se pase nada por parametro al metodo write()

public class ServerMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, RMSG.getCommand() + " <message>");
        requireValidString(commandContext, "message", REGEX);
        writeServerMessage();
    }

}
