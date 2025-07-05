package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeBanIP;
import static org.aoclient.network.protocol.command.GameCommand.BAN_IP;

public class BanIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, BAN_IP.getCommand() + " <ip|nick> <reason>");
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
