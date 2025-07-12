package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.showGuildMessages;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_MSG_HISTORY;

public class ShowGuildMessagesCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(GUILD_MSG_HISTORY));
        String name = commandContext.getArgument(0);
        showGuildMessages(name);
    }

}
