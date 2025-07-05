package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCheckSlot;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW_PLAYER_SLOT;

public class CheckSlotCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, SHOW_PLAYER_SLOT.getCommand() + " <nick> <slot>");
        requireString(commandContext, 0, "nick");
        requireInteger(commandContext, 1, "slot");

        String nick = commandContext.getArgument(0);
        int slot = Integer.parseInt(commandContext.getArgument(1));

        writeCheckSlot(nick, slot);
    }

}
