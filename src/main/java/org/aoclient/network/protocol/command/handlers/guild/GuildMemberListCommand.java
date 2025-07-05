package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildMemberList;
import static org.aoclient.network.protocol.command.GameCommand.GUILD_MEMBER_LIST;

public class GuildMemberListCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, GUILD_MEMBER_LIST.getCommand() + " <name>");
        String name = commandContext.getArgument(0);
        writeGuildMemberList(name);
    }

}
