package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.bankExtractGold;
import static org.aoclient.network.protocol.command.metadata.GameCommand.EXTRACT_GOLD;

public class BankExtractGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(commandContext, 1, EXTRACT_GOLD.getCommand() + " <quantity>");
        requireInteger(commandContext, 0, "quantity");

        int quantity = Integer.parseInt(commandContext.getArgument(0));

        /* Esta condicion se va a cumplir aunque la cantidad especificada sea menor al oro depositado, ya que si no se creo la
         * ventana del banco FBank primero, entonces el valor de goldDeposited es 0. */
        if (quantity > FBank.goldDeposited) {
            showError("You don't have enough gold!");
            return;
        }

        bankExtractGold(Integer.parseInt(commandContext.argumentsRaw()));
    }

}
