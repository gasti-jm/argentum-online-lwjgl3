package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.partyMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_MSG;

public class PartyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, PARTY_MSG.getCommand() + " <message>");
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        partyMessage(message);
    }

}
