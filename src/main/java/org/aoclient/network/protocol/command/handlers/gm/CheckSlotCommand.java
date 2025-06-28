package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCheckSlot;

@Command("/slot")
@SuppressWarnings("unused")
public class CheckSlotCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/slot <nick> <slot>");
        requireString(context, 0, "nick");
        requireInteger(context, 1, "slot");

        String nick = context.getArgument(0);
        int slot = Integer.parseInt(context.getArgument(1));

        writeCheckSlot(nick, slot);
    }

}
