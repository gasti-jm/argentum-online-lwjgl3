package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;

@Command("/borrarpena")
@SuppressWarnings("unused")
public class RemovePunishmentCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, "/borrarpena <nick> <minutes> <newPunishment>");
        requireString(commandContext, 0, "nick");
        requireInteger(commandContext, 1, "minutes");
        requireString(commandContext, 2, "newPunishment");
        String nick = commandContext.getArgument(0);
        int minutes = Integer.parseInt(commandContext.getArgument(1));
        String newPunishment = commandContext.getArgument(2);
        writeRemovePunishment(nick, minutes, newPunishment);
    }

}
