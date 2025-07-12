package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.core.CommandValidator;

import static org.aoclient.network.protocol.Protocol.unbanIP;
import static org.aoclient.network.protocol.command.metadata.GameCommand.UNBANIP;

public class UnbanIpCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(UNBANIP));
        String ip = commandContext.getArgument(0);
        if (!CommandValidator.isValidIPv4(ip)) showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");
        unbanIP(CommandValidator.parseIPv4ToArray(ip));
    }

}
