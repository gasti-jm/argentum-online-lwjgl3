package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeShowGuildMessages;

@Command("/showcmsg")
@SuppressWarnings("unused")
public class ShowGuildMessagesCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/showcmsg <name>");
        String name = textContext.getArgument(0);
        writeShowGuildMessages(name);
    }

}
