package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterPassword;

@Command("/apass")
@SuppressWarnings("unused")
public class AlterPasswordCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, "/apass <pjsinpass> <pjconpass>");
        requireString(textContext, 0, "pjsinpass");
        requireString(textContext, 1, "pjconpass");

        String playerWithoutPassword = textContext.getArgument(0);
        String playerWithPassword = textContext.getArgument(1);

        writeAlterPassword(playerWithoutPassword, playerWithPassword);
    }

}
