package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.chaosLegionKick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REMOVE_PLAYER_FROM_CHAOS;

public class ChaosLegionKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(REMOVE_PLAYER_FROM_CHAOS));
        requireString(commandContext, 0, "nick");
        String nick = commandContext.getArgument(0);
        chaosLegionKick(nick);
    }

}
