package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.bankDepositGold;
import static org.aoclient.network.protocol.command.metadata.GameCommand.DEPOSIT;

public class BankDepositGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(commandContext, 1, DEPOSIT.getCommand() + " <quantity>");
        requireInteger(commandContext, 0, "quantity");

        int quantity = Integer.parseInt(commandContext.getArgument(0));

        if (quantity > user.getUserGLD()) {
            showError("You don't have enough gold!");
            return;
        }

        bankDepositGold(quantity);
    }

}
