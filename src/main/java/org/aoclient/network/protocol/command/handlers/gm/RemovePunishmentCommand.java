package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;
import static org.aoclient.network.protocol.command.metadata.GameCommand.REMOVE_PUNISHMENT;

public class RemovePunishmentCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, REMOVE_PUNISHMENT.getCommand() + " <nick> <minutes> <newPunishment>");
        requireString(commandContext, 0, "nick");
        requireInteger(commandContext, 1, "minutes");
        requireString(commandContext, 2, "newPunishment");
        String nick = commandContext.getArgument(0);
        int minutes = Integer.parseInt(commandContext.getArgument(1));
        String newPunishment = commandContext.getArgument(2);
        writeRemovePunishment(nick, minutes, newPunishment);
    }

}
