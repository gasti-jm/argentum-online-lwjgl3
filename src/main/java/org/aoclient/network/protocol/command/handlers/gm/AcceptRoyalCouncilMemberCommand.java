package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAcceptRoyalCouncilMember;

public class AcceptRoyalCouncilMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/aceptconse <nick>");
        String nick = context.getArgument(0);
        writeAcceptRoyalCouncilMember(nick);
    }

}
