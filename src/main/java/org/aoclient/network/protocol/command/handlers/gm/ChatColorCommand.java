package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChatColor;

@Command("/chatcolor")
@SuppressWarnings("unused")
public class ChatColorCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (!commandContext.hasArguments()) {
            writeChatColor(0, 255, 0);
            return;
        }

        requireArguments(commandContext, 3, "/chatcolor <r> <g> <b>");
        requireInteger(commandContext, 0, "r");
        requireInteger(commandContext, 1, "g");
        requireInteger(commandContext, 2, "b");

        int r = Integer.parseInt(commandContext.getArgument(0));
        int g = Integer.parseInt(commandContext.getArgument(1));
        int b = Integer.parseInt(commandContext.getArgument(2));

        writeChatColor(r, g, b);
    }

}
