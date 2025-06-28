package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRoyaleArmyMessage;

@Command("/realmsg")
@SuppressWarnings("unused")
public class RoyaleArmyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/realmsg <message>");
        String nick = context.getArgument(0);
        writeRoyaleArmyMessage(nick);
    }

}
