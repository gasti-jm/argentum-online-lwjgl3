package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.chatColor;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CHAT_COLOR;

public class ChatColorCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (!commandContext.hasArguments()) {
            chatColor(0, 255, 0);
            return;
        }

        requireArguments(commandContext, 3, getCommandUsage(CHAT_COLOR));
        requireInteger(commandContext, 0, "r");
        requireInteger(commandContext, 1, "g");
        requireInteger(commandContext, 2, "b");

        int r = Integer.parseInt(commandContext.getArgument(0));
        int g = Integer.parseInt(commandContext.getArgument(1));
        int b = Integer.parseInt(commandContext.getArgument(2));

        chatColor(r, g, b);
    }

}
