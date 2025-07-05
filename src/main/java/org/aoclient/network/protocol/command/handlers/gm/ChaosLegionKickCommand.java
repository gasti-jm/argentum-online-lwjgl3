package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosLegionKick;
import static org.aoclient.network.protocol.command.GameCommand.REMOVE_PLAYER_FROM_CHAOS;

public class ChaosLegionKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, REMOVE_PLAYER_FROM_CHAOS.getCommand() + " <nick>");
        requireString(commandContext, 0, "nick");
        String nick = commandContext.getArgument(0);
        writeChaosLegionKick(nick);
    }

}
