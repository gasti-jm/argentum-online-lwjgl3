package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRoyaleArmyMessage;
import static org.aoclient.network.protocol.command.GameCommand.REALMSG;

public class RoyaleArmyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, REALMSG.getCommand() + " <message>");
        String nick = commandContext.getArgument(0);
        writeRoyaleArmyMessage(nick);
    }

}
