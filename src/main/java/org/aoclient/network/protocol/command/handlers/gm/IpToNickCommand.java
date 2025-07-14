package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.core.CommandValidator;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.IpToNick;
import static org.aoclient.network.protocol.command.metadata.GameCommand.IP_TO_NICK;

public class IpToNickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(IP_TO_NICK));

        String ip = commandContext.argumentsRaw();

        if (!CommandValidator.isValidIPv4(ip)) showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");

        int[] ipArray = CommandValidator.parseIPv4ToArray(ip);
        if (ipArray == null) showError("Error parsing IP address.");
        else IpToNick(ipArray);
    }

}

