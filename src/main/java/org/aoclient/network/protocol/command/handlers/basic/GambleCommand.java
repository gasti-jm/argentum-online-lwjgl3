package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeGamble;
import static org.aoclient.network.protocol.command.metadata.GameCommand.BET;

public class GambleCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(commandContext, 1, BET.getCommand() + " <amount>");
        requireShort(commandContext, 0, "amount");

        short amount = Short.parseShort(commandContext.getArgument(0));

        writeGamble(amount);
    }

}
