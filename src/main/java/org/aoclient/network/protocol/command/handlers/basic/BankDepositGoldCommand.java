package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBankDepositGold;
import static org.aoclient.network.protocol.command.GameCommand.DEPOSIT;

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

        writeBankDepositGold(quantity);
    }

}
