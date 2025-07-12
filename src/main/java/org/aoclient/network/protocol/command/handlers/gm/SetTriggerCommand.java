package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.askTrigger;
import static org.aoclient.network.protocol.Protocol.setTrigger;

public class SetTriggerCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.hasArguments()) {
            // Modo: establecer trigger con numero especifico
            requireInteger(commandContext, 0, "trigger");
            int triggerNumber = Integer.parseInt(commandContext.getArgument(0));
            setTrigger(triggerNumber);
        } else askTrigger(); // Modo: solicitar informacion sobre trigger actual
    }

}
