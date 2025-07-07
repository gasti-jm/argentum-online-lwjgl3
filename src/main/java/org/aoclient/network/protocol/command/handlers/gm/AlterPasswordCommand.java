package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.alterPassword;
import static org.aoclient.network.protocol.command.metadata.GameCommand.APASS;

public class AlterPasswordCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, APASS.getCommand() + " <pjsinpass> <pjconpass>");
        requireString(commandContext, 0, "pjsinpass");
        requireString(commandContext, 1, "pjconpass");

        String playerWithoutPassword = commandContext.getArgument(0);
        String playerWithPassword = commandContext.getArgument(1);

        alterPassword(playerWithoutPassword, playerWithPassword);
    }

}
