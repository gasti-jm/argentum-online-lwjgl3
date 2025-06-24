package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandValidator;

import static org.aoclient.network.protocol.Protocol.writeAlterMail;

public class AlterMailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/aemail <nick>-<newmail>");

        // El comando usa un formato especial: nick-email en un solo argumento
        String[] parts = CommandValidator.INSTANCE.AEMAILSplit(context.getArgumentsRaw());
        // Se podria reemplazar AEMAILSplit() por String[] parts = context.getArgumentsRaw().split("-", 2);

        if (parts[0].isEmpty()) showError("Incorrect format. Usage: /aemail <nick>-<newmail>");

        // Valida que el nick no este vacio
        String nick = parts[0].trim();
        String email = parts[1].trim();

        if (nick.isEmpty()) showError("Incorrect nick, must be a non-empty username.");
        if (email.isEmpty()) showError("Incorrect email, must be a non-empty username.");

        writeAlterMail(nick, email);

    }

}
