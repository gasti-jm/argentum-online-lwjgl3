package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRemoveCharFromGuild;

@Command("/rajarclan")
@SuppressWarnings("unused")
public class RemoveCharFromGuildCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/rajarclan <nick>");
        String nick = textContext.getArgument(0);
        writeRemoveCharFromGuild(nick);
    }

}
