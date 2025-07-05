package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAcceptChaosCouncilMember;
import static org.aoclient.network.protocol.command.GameCommand.ACEPT_CONSE_CHAOS;

public class AcceptChaosCouncilMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, ACEPT_CONSE_CHAOS.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeAcceptChaosCouncilMember(nick);
    }

}
