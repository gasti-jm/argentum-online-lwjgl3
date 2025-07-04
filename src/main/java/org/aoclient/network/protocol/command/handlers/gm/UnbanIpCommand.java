package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeUnbanIP;

public class UnbanIpCommand extends BaseCommandHandler {

    private static final String USAGE = "/unbanip <ip>";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, USAGE);

        String ip = context.getArgument(0);
        requireValidIP(ip);

        writeUnbanIP(CommandValidator.INSTANCE.parseIPv4ToArray(ip));
    }

    /**
     * Valida que la IP proporcionada sea una IPv4 válida.
     *
     * @param ip dirección IP a validar
     * @throws CommandException si la IP no es válida
     */
    private void requireValidIP(String ip) throws CommandException {
        if (!CommandValidator.INSTANCE.isValidIPv4(ip))
            showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");
    }

}
