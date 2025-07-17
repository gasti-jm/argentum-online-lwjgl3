package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playerSlot;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAYER_SLOT;

public class PlayerSlotCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, getCommandUsage(PLAYER_SLOT));
        requireString(commandContext, 0, "player");
        requireInteger(commandContext, 1, "slot");

        String player = commandContext.getArgument(0);
        int slot = Integer.parseInt(commandContext.getArgument(1));

        playerSlot(player, slot);
    }

}
