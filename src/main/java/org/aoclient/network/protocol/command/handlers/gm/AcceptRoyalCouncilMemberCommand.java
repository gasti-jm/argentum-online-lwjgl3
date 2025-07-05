package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAcceptRoyalCouncilMember;
import static org.aoclient.network.protocol.command.GameCommand.ACEPT_CONSE;

public class AcceptRoyalCouncilMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, ACEPT_CONSE.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeAcceptRoyalCouncilMember(nick);
    }

}
