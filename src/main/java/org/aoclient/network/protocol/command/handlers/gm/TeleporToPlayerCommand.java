package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.teleportToPlayer;
import static org.aoclient.network.protocol.command.metadata.GameCommand.TELEPORT_TO_PLAYER;

public class TeleporToPlayerCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(TELEPORT_TO_PLAYER));
        String player = commandContext.getArgument(0);
        teleportToPlayer(player);
    }

}
