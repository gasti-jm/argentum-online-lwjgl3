package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.core.CommandValidator;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.unbanIp;
import static org.aoclient.network.protocol.command.metadata.GameCommand.UNBAN_IP;

public class UnbanIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(UNBAN_IP));
        String ip = commandContext.getArgument(0);
        if (!CommandValidator.isValidIPv4(ip)) showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");
        unbanIp(CommandValidator.parseIPv4ToArray(ip));
    }

}
