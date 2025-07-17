package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.royalArmyMessage;
import static org.aoclient.network.protocol.command.metadata.GameCommand.ROYAL_ARMY_MSG;

public class RoyalArmyMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(ROYAL_ARMY_MSG));
        String message = commandContext.getArgument(0);
        royalArmyMessage(message);
    }

}
