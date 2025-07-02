package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeImperialArmour;

@Command("/ai")
@SuppressWarnings("unused")
public class ImperialArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/ai <armor_id> <object_id>");
        requireInteger(commandContext, 0, "armor_id");
        requireInteger(commandContext, 1, "object_id");

        int armorId = Integer.parseInt(commandContext.getArgument(0));
        short objectId = Short.parseShort(commandContext.getArgument(1));

        writeImperialArmour(armorId, objectId);
    }

}
