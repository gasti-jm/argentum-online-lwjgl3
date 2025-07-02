package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAcceptChaosCouncilMember;

@Command("/aceptconsecaos")
@SuppressWarnings("unused")
public class AcceptChaosCouncilMemberCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/aceptconsecaos <nick>");
        String nick = commandContext.getArgument(0);
        writeAcceptChaosCouncilMember(nick);
    }

}
