package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeShowGuildMessages;
import static org.aoclient.network.protocol.command.GameCommand.GUILD_MSG_HISTORY;

public class ShowGuildMessagesCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_MSG_HISTORY.getCommand() + " <name>");
        String name = commandContext.getArgument(0);
        writeShowGuildMessages(name);
    }

}
