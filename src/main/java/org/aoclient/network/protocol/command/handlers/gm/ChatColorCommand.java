package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChatColor;

@Command("/chatcolor")
@SuppressWarnings("unused")
public class ChatColorCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        if (!textContext.hasArguments()) {
            writeChatColor(0, 255, 0);
            return;
        }

        requireArguments(textContext, 3, "/chatcolor <r> <g> <b>");
        requireInteger(textContext, 0, "r");
        requireInteger(textContext, 1, "g");
        requireInteger(textContext, 2, "b");

        int r = Integer.parseInt(textContext.getArgument(0));
        int g = Integer.parseInt(textContext.getArgument(1));
        int b = Integer.parseInt(textContext.getArgument(2));

        writeChatColor(r, g, b);
    }

}
