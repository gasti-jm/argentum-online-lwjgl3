package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterPassword;

public class AlterPasswordCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, "/apass <pjsinpass> <pjconpass>");
        requireString(context, 0, "pjsinpass");
        requireString(context, 1, "pjconpass");

        String playerWithoutPassword = context.getArgument(0);
        String playerWithPassword = context.getArgument(1);

        writeAlterPassword(playerWithoutPassword, playerWithPassword);
    }

}
