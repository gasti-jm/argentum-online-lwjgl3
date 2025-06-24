package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreaturesInMap;

public class CreaturesInMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/nene <map>");
        requireInteger(context, 0, "map");
        short map = Short.parseShort(context.getArgument(0));
        writeCreaturesInMap(map);
    }

}
