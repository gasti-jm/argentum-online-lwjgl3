package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemoveCharFromGuild;

@Command("/rajarclan")
@SuppressWarnings("unused")
public class RemoveCharFromGuildCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/rajarclan <nick>");
        String nick = commandContext.getArgument(0);
        writeRemoveCharFromGuild(nick);
    }

}
