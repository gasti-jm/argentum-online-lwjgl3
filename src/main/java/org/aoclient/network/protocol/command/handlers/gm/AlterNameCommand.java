package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterName;

@Command("/aname")
@SuppressWarnings("unused")
public class AlterNameCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/aname <origin> <dest>");
        requireString(textContext, 0, "origin");
        requireString(textContext, 1, "dest");

        String origin = textContext.getArgument(0);
        String dest = textContext.getArgument(1);

        writeAlterName(origin, dest);

    }

}
