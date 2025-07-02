package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosArmour;

@Command("/ac")
@SuppressWarnings("unused")
public class ChaosArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/ac <armor> <object>");
        requireInteger(textContext, 0, "armor");
        requireShort(textContext, 1, "object");

        int armor = Integer.parseInt(textContext.getArgument(0));
        short object = Short.parseShort(textContext.getArgument(1));

        writeChaosArmour(armor, object);
    }

}
