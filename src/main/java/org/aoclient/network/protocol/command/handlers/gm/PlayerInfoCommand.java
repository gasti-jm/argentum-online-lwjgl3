package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playerInfo;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAYER_INFO;

public class PlayerInfoCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PLAYER_INFO));
        String player = commandContext.getArgument(0);
        playerInfo(player);
    }

}
