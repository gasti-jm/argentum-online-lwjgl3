package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChatColor;

public class ChatColorCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (!context.hasArguments()) {
            writeChatColor(0, 255, 0);
            return;
        }

        requireArguments(context, 3, "/chatcolor <r> <g> <b>");
        requireInteger(context, 0, "r");
        requireInteger(context, 1, "g");
        requireInteger(context, 2, "b");

        int r = Integer.parseInt(context.getArgument(0));
        int g = Integer.parseInt(context.getArgument(1));
        int b = Integer.parseInt(context.getArgument(2));

        writeChatColor(r, g, b);
    }

}
