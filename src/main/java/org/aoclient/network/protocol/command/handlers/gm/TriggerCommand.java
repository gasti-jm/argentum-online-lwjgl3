package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.setTrigger;
import static org.aoclient.network.protocol.Protocol.showTrigger;

public class TriggerCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.hasArguments()) {
            requireInteger(commandContext, 0, "trigger");
            int trigger = Integer.parseInt(commandContext.getArgument(0));
            setTrigger(trigger);
        } else showTrigger();
    }

}
