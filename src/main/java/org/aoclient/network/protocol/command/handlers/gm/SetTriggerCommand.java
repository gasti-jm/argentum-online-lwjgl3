package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAskTrigger;
import static org.aoclient.network.protocol.Protocol.writeSetTrigger;

@Command("/trigger")
@SuppressWarnings("unused")
public class SetTriggerCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        if (textContext.hasArguments()) {
            // Modo: establecer trigger con numero especifico
            requireInteger(textContext, 0, "trigger number");
            int triggerNumber = Integer.parseInt(textContext.getArgument(0));
            writeSetTrigger(triggerNumber);
        } else writeAskTrigger(); // Modo: solicitar informacion sobre trigger actual
    }

}
