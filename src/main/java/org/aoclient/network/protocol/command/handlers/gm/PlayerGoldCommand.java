package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playerGold;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAYER_GOLD;

public class PlayerGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PLAYER_GOLD));
        String player = commandContext.getArgument(0);
        playerGold(player);
    }

}
