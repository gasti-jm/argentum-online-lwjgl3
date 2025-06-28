package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;

public class RemovePunishmentCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, "/borrarpena <nick> <minutes> <newPunishment>");
        requireString(context, 0, "nick");
        requireInteger(context, 1, "minutes");
        requireString(context, 2, "newPunishment");
        String nick = context.getArgument(0);
        int minutes = Integer.parseInt(context.getArgument(1));
        String newPunishment = context.getArgument(2);
        writeRemovePunishment(nick, minutes, newPunishment);
    }

}
