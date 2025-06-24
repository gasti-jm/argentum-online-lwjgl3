package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGamble;

public class GambleCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(context, 1, "/apostar <amount>");
        requireShort(context, 0, "amount");

        short amount = Short.parseShort(context.getArgument(0));

        writeGamble(amount);
    }

}
