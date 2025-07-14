package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.changeNick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CHANGE_NICK;

public class ChangeNickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, getCommandUsage(CHANGE_NICK));
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "newNick");

        String nick = commandContext.getArgument(0);
        String newNick = commandContext.getArgument(1);

        changeNick(nick, newNick);

    }

}
