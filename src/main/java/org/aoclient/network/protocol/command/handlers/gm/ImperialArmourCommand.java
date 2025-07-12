package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.imperialArmour;
import static org.aoclient.network.protocol.command.metadata.GameCommand.AI;

public class ImperialArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, getCommandUsage(AI));
        requireInteger(commandContext, 0, "armor_id");
        requireInteger(commandContext, 1, "object_id");

        int armorId = Integer.parseInt(commandContext.getArgument(0));
        short objectId = Short.parseShort(commandContext.getArgument(1));

        imperialArmour(armorId, objectId);
    }

}
