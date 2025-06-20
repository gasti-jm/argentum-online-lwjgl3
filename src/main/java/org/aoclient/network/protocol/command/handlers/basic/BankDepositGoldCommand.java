package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeBankDepositGold;

public class BankDepositGoldCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.isDead())
            console.addMsgToConsole(new String("You are dead!".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        else {
            if (context.hasArguments()) {
                if (validator.isValidNumber(context.getArgumentsRaw(), NumericType.LONG))
                    writeBankDepositGold(Integer.parseInt(context.getArgumentsRaw()));
                else
                    console.addMsgToConsole(new String("Incorrect amount. Please use \"/DEPOSITAR amount\".".getBytes(), StandardCharsets.UTF_8),
                            false, true, new RGBColor());
            }
        }
    }

}
