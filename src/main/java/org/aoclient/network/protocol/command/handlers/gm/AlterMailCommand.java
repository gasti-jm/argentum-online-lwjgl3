package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeAlterMail;

public class AlterMailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/aemail <nick>-<newmail>");

        // El comando usa un formato especial: nick-email en un solo argumento
        String[] parts = AEMAILSplit(commandContext.argumentsRaw());
        // Se podria reemplazar AEMAILSplit() por String[] parts = context.getArgumentsRaw().split("-", 2);

        if (parts[0].isEmpty()) showError("Incorrect format. Usage: /aemail <nick>-<newmail>");

        // Valida que el nick no este vacio
        String nick = parts[0].trim();
        String email = parts[1].trim();

        if (nick.isEmpty()) showError("Incorrect nick, must be a non-empty username.");
        if (email.isEmpty()) showError("Incorrect email, must be a non-empty username.");

        writeAlterMail(nick, email);

    }

    // FIXME Esta cortando mal la cadena ejemplo: "jua.chr" falta la n
    public String[] AEMAILSplit(String text) {
        String[] tmpArr = new String[2];
        byte Pos;
        Pos = (byte) text.indexOf("-");
        if (Pos != 0) {
            tmpArr[0] = text.substring(0, Pos - 1);
            tmpArr[1] = text.substring(Pos + 1);
        } else tmpArr[0] = "";
        return tmpArr;
    }

}
