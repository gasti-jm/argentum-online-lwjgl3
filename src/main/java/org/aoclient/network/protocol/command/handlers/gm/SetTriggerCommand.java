package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAskTrigger;
import static org.aoclient.network.protocol.Protocol.writeSetTrigger;

public class SetTriggerCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.hasArguments()) {
            // Modo: establecer trigger con numero especifico
            requireInteger(commandContext, 0, "trigger number");
            int triggerNumber = Integer.parseInt(commandContext.getArgument(0));
            writeSetTrigger(triggerNumber);
        } else writeAskTrigger(); // Modo: solicitar informacion sobre trigger actual
    }

}
