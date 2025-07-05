package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.chaosArmour;
import static org.aoclient.network.protocol.command.metadata.GameCommand.AC;

public class ChaosArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, AC.getCommand() + " <armor> <object>");
        requireInteger(commandContext, 0, "armor");
        requireShort(commandContext, 1, "object");

        int armor = Integer.parseInt(commandContext.getArgument(0));
        short object = Short.parseShort(commandContext.getArgument(1));

        chaosArmour(armor, object);
    }

}
