package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.goNearby;
import static org.aoclient.network.protocol.command.metadata.GameCommand.TELEPORT_NEAR_TO_PLAYER;

public class GoNearbyCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, TELEPORT_NEAR_TO_PLAYER.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        goNearby(nick);
    }

}
