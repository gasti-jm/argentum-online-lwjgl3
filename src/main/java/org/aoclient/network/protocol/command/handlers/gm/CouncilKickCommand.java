package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCouncilKick;
import static org.aoclient.network.protocol.command.GameCommand.KICK_CONSE;

public class CouncilKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, KICK_CONSE.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeCouncilKick(nick);
    }

}
