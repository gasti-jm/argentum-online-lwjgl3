package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosLegionKick;

public class ChaosLegionKickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/nocaos <nick>");
        requireString(context, 0, "nick");
        String nick = context.getArgument(0);
        writeChaosLegionKick(nick);
    }

}
