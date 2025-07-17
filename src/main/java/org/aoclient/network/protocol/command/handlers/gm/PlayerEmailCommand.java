package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playerEmail;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAYER_EMAIL;

public class PlayerEmailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PLAYER_EMAIL));
        String player = commandContext.getArgument(0);
        playerEmail(player);
    }

}
