package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeBanIP;

@Command("/banip")
@SuppressWarnings("unused")
public class BanIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, "/banip <ip|nick> <reason>");
        requireString(commandContext, 0, "ip|nick");
        requireString(commandContext, 1, "reason");

        String ipOrNick = commandContext.getArgument(0);
        String reason = commandContext.getArgument(1);

        if (CommandValidator.isValidIPv4(ipOrNick))
            writeBanIP(true, CommandValidator.parseIPv4ToArray(ipOrNick), "", reason); // Banea por IP
        else
            writeBanIP(false, CommandValidator.parseIPv4ToArray("0.0.0.0"), ipOrNick, reason); // Banea por nick (buscar IP del jugador)

    }

}
