package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreaturesInMap;
import static org.aoclient.network.protocol.command.GameCommand.SHOW_MOBS;

public class CreaturesInMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, SHOW_MOBS.getCommand() + " <map>");
        requireInteger(commandContext, 0, "map");
        short map = Short.parseShort(commandContext.getArgument(0));
        writeCreaturesInMap(map);
    }

}
