package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreaturesInMap;

@Command("/nene")
@SuppressWarnings("unused")
public class CreaturesInMapCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/nene <map>");
        requireInteger(textContext, 0, "map");
        short map = Short.parseShort(textContext.getArgument(0));
        writeCreaturesInMap(map);
    }

}
