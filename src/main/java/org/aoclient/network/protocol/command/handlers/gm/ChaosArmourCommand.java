package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosArmour;

public class ChaosArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/ac <armor> <object>");
        requireInteger(commandContext, 0, "armor");
        requireShort(commandContext, 1, "object");

        int armor = Integer.parseInt(commandContext.getArgument(0));
        short object = Short.parseShort(commandContext.getArgument(1));

        writeChaosArmour(armor, object);
    }

}
