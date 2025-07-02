package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeImperialArmour;

@Command("/ai")
@SuppressWarnings("unused")
public class ImperialArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/ai <armor_id> <object_id>");
        requireInteger(textContext, 0, "armor_id");
        requireInteger(textContext, 1, "object_id");

        int armorId = Integer.parseInt(textContext.getArgument(0));
        short objectId = Short.parseShort(textContext.getArgument(1));

        writeImperialArmour(armorId, objectId);
    }

}
