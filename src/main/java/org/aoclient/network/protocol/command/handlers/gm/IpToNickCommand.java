package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeIPToNick;

/**
 * Comando para obtener el nickname asociado a una dirección IP.
 * <p>
 * Uso: {@code /ip2nick <ip>}
 * <p>
 * Parametros:
 * <ul>
 * <li>ip: Dirección IP en formato IPv4 (ej: 192.168.1.1)
 * </ul>
 * <p>
 * Ejemplo: {@code /ip2nick 192.168.1.100} - Buscar nickname asociado a esta IP
 */

@Command("/ip2nick")
@SuppressWarnings("unused")
public class IpToNickCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/ip2nick <ip>");

        String ip = commandContext.argumentsRaw();

        if (!CommandValidator.isValidIPv4(ip)) showError("Invalid IP address, must be a valid IPv4 address (e.g., 192.168.1.1).");

        int[] ipArray = CommandValidator.parseIPv4ToArray(ip);
        if (ipArray == null) showError("Error parsing IP address.");

        writeIPToNick(ipArray);
    }

}

