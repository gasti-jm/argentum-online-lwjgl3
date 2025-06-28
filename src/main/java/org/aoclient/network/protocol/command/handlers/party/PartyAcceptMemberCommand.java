package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePartyAcceptMember;

@Command("/acceptparty")
@SuppressWarnings("unused")
public class PartyAcceptMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/acceptparty <nick>");
        String nick = context.getArgument(0);
        writePartyAcceptMember(nick);
    }

}
