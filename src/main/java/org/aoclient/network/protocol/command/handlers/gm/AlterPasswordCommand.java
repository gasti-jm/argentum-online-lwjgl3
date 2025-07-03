package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterPassword;

public class AlterPasswordCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/apass <pjsinpass> <pjconpass>");
        requireString(commandContext, 0, "pjsinpass");
        requireString(commandContext, 1, "pjconpass");

        String playerWithoutPassword = commandContext.getArgument(0);
        String playerWithPassword = commandContext.getArgument(1);

        writeAlterPassword(playerWithoutPassword, playerWithPassword);
    }

}
