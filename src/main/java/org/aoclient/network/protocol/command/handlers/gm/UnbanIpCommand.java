package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeUnbanIP;

@Command("/unbanip")
@SuppressWarnings("unused")
public class UnbanIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/unbanip <ip>");
        String ip = context.getArgument(0);
        if (!CommandValidator.isValidIPv4(ip)) showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");
        writeUnbanIP(CommandValidator.parseIPv4ToArray(ip));
    }

}
