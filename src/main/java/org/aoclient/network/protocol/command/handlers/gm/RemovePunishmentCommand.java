package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;

public class RemovePunishmentCommand extends BaseCommandHandler {

    private static final String USAGE = "/borrarpena <nick> <minutes> <newPunishment>";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, USAGE);

        String nick = context.getArgument(0);
        requireString(context, 0, "nick");

        requireInteger(context, 1, "minutes");
        int minutes = Integer.parseInt(context.getArgument(1));

        String newPunishment = context.getArgument(2);
        requireString(context, 2, "newPunishment");

        writeRemovePunishment(nick, minutes, newPunishment);
    }

}
