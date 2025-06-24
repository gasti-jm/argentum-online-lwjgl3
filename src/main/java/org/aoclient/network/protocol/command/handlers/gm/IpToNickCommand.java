package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeIPToNick;

/**
 * Comando para obtener el nickname asociado a una dirección IP.
 * <p>
 * Uso: {@code /ip2nick <ip_address>}
 * <p>
 * Parametros:
 * <ul>
 * <li>ip_address: Dirección IP en formato IPv4 (ej: 192.168.1.1)
 * </ul>
 * <p>
 * Ejemplo: {@code /ip2nick 192.168.1.100} - Buscar nickname asociado a esta IP
 */

public class IpToNickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/ip2nick <ip_address>");

        String ipAddress = context.getArgumentsRaw();

        if (!CommandValidator.INSTANCE.isValidIPv4(ipAddress))
            showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");

        int[] ipArray = CommandValidator.INSTANCE.parseIPv4ToArray(ipAddress);
        if (ipArray == null) showError("Error parsing IP address.");

        writeIPToNick(ipArray);
    }

}

