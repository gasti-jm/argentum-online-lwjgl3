package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemovePunishment;

@Command("/borrarpena")
@SuppressWarnings("unused")
public class RemovePunishmentCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 3, "/borrarpena <nick> <minutes> <newPunishment>");
        requireString(textContext, 0, "nick");
        requireInteger(textContext, 1, "minutes");
        requireString(textContext, 2, "newPunishment");
        String nick = textContext.getArgument(0);
        int minutes = Integer.parseInt(textContext.getArgument(1));
        String newPunishment = textContext.getArgument(2);
        writeRemovePunishment(nick, minutes, newPunishment);
    }

}
