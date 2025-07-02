package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCheckSlot;

@Command("/slot")
@SuppressWarnings("unused")
public class CheckSlotCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/slot <nick> <slot>");
        requireString(textContext, 0, "nick");
        requireInteger(textContext, 1, "slot");

        String nick = textContext.getArgument(0);
        int slot = Integer.parseInt(textContext.getArgument(1));

        writeCheckSlot(nick, slot);
    }

}
