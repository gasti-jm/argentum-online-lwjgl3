package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRoyalArmyKick;
import static org.aoclient.network.protocol.command.GameCommand.NO_REAL;

public class RoyalArmyKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, NO_REAL.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRoyalArmyKick(nick);
    }

}
