package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGoNearby;
import static org.aoclient.network.protocol.command.GameCommand.TELEPORT_NEAR_TO_PLAYER;

public class GoNearbyCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, TELEPORT_NEAR_TO_PLAYER.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeGoNearby(nick);
    }

}
