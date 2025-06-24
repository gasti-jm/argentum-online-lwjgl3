package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeImperialArmour;

public class ImperialArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/ai <armor_id> <object_id>");
        requireInteger(context, 0, "armor_id");
        requireInteger(context, 1, "object_id");

        int armorId = Integer.parseInt(context.getArgument(0));
        short objectId = Short.parseShort(context.getArgument(1));

        writeImperialArmour(armorId, objectId);
    }

}
