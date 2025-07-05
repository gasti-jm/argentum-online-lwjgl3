package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharInventory;
import static org.aoclient.network.protocol.command.GameCommand.PLAYER_INV;

public class RequestCharInventoryCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, PLAYER_INV.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRequestCharInventory(nick);
    }

}
