package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.creaturesInMap;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW_MOBS;

public class CreaturesInMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(SHOW_MOBS));
        requireInteger(commandContext, 0, "map");
        short map = Short.parseShort(commandContext.getArgument(0));
        creaturesInMap(map);
    }

}
