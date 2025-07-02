package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBankExtractGold;

@Command("/retirar")
@SuppressWarnings("unused")
public class BankExtractGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(textContext, 1, "/retirar <quantity>");
        requireInteger(textContext, 0, "quantity");

        int quantity = Integer.parseInt(textContext.getArgument(0));

        /* Esta condicion se va a cumplir aunque la cantidad especificada sea menor al oro depositado, ya que si no se creo la
         * ventana del banco FBank primero, entonces el valor de goldDeposited es 0. */
        if (quantity > FBank.goldDeposited) {
            showError("You don't have enough gold!");
            return;
        }

        writeBankExtractGold(Integer.parseInt(textContext.argumentsRaw()));
    }

}
