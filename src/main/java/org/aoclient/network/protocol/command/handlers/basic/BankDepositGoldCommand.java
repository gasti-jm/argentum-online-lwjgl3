package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBankDepositGold;

public class BankDepositGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(context, 1, "/depositar <quantity>");
        requireInteger(context, 0, "quantity");

        int quantity = Integer.parseInt(context.getArgument(0));

        if (quantity > user.getUserGLD()) {
            showError("You don't have enough gold!");
            return;
        }

        writeBankDepositGold(quantity);
    }

}
