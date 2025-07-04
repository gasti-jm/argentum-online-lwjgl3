package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBankExtractGold;

public class BankExtractGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(context, 1, "/retirar <quantity>");
        requireInteger(context, 0, "quantity");

        int quantity = Integer.parseInt(context.getArgument(0));

        /* Esta condicion se va a cumplir aunque la cantidad especificada sea menor al oro depositado, ya que si no se creo la
         * ventana del banco FBank primero, entonces el valor de goldDeposited es 0. */
        if (quantity > FBank.goldDeposited) {
            showError("You don't have enough gold!");
            return;
        }

        writeBankExtractGold(Integer.parseInt(context.getArgumentsRaw()));
    }

}
