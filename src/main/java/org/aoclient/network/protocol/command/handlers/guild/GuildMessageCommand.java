package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.guildMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.GUILD_MSG;

public class GuildMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, UNLIMITED_ARGUMENTS, GUILD_MSG.getCommand() + " <message>");
        requireValidString(commandContext, "message", REGEX);
        String message = commandContext.argumentsRaw().trim();
        guildMessage(message);
    }

}
