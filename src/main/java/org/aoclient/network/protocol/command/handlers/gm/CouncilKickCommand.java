package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCouncilKick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.KICK_CONSE;

public class CouncilKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, KICK_CONSE.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeCouncilKick(nick);
    }

}
