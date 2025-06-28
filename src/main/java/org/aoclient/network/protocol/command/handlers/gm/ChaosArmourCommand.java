package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosArmour;

@Command("/ac")
@SuppressWarnings("unused")
public class ChaosArmourCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/ac <armor> <object>");
        requireInteger(context, 0, "armor");
        requireShort(context, 1, "object");

        int armor = Integer.parseInt(context.getArgument(0));
        short object = Short.parseShort(context.getArgument(1));

        writeChaosArmour(armor, object);
    }

}
