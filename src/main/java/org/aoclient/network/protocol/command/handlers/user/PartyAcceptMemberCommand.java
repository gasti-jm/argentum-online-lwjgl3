package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.partyAcceptMember;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PARTY_ACCEPT;

public class PartyAcceptMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PARTY_ACCEPT));
        String nick = commandContext.getArgument(0);
        partyAcceptMember(nick);
    }

}
