package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.showMobs;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW_MOBS;

public class ShowMobsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(SHOW_MOBS));
        requireInteger(commandContext, 0, "map");
        short map = Short.parseShort(commandContext.getArgument(0));
        showMobs(map);
    }

}
