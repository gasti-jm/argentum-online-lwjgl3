package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.gui.forms.FBank;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.extractGold;
import static org.aoclient.network.protocol.command.metadata.GameCommand.EXTRACT_GOLD;

public class ExtractGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(commandContext, 1, getCommandUsage(EXTRACT_GOLD));
        requireInteger(commandContext, 0, "amount");

        int amount = Integer.parseInt(commandContext.getArgument(0));

        /* Esta condicion se va a cumplir aunque la cantidad especificada sea menor al oro depositado, ya que si no se creo la
         * ventana del banco (FBank) primero, entonces el valor de goldDeposited es 0. */
        if (amount > FBank.goldDeposited) {
            showError("You don't have enough gold!");
            return;
        }

        extractGold(amount);
    }

}
