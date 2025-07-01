package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartyMessage;

@Command("/pmsg")
@SuppressWarnings("unused")
public class PartyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/pmsg <message>");
        requireValidString(context, "message", REGEX);
        String message = context.argumentsRaw().trim();
        writePartyMessage(message);
    }

}
