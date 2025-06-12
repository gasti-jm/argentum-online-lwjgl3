package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writePartyAcceptMember;

public class PartyAcceptMemberCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writePartyAcceptMember(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing arguments. Usage: /acceptparty <nick>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
