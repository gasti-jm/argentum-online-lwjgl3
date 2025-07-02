package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGamble;

@Command("/apostar")
@SuppressWarnings("unused")
public class GambleCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(commandContext, 1, "/apostar <amount>");
        requireShort(commandContext, 0, "amount");

        short amount = Short.parseShort(commandContext.getArgument(0));

        writeGamble(amount);
    }

}
