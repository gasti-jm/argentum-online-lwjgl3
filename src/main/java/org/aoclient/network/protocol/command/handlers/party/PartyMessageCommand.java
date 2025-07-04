package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartyMessage;

public class PartyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, -1, "/pmsg <message>");
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        writePartyMessage(message);
    }

}
