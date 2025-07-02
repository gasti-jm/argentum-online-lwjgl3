package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGuildMemberList;

@Command("/miembrosclan")
@SuppressWarnings("unused")
public class GuildMemberListCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/miembrosclan <name>");
        String name = textContext.getArgument(0);
        writeGuildMemberList(name);
    }

}
