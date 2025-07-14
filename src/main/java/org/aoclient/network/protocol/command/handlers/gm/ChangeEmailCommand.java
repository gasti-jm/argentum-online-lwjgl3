package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.changeEmail;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CHANGE_EMAIL;

public class ChangeEmailCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(CHANGE_EMAIL));

        // El comando usa un formato especial: nick-email en un solo argumento
        String[] parts = AEMAILSplit(commandContext.argumentsRaw());
        // Se podria reemplazar AEMAILSplit() por String[] parts = context.getArgumentsRaw().split("-", 2);

        if (parts[0].isEmpty()) showError("Incorrect format. Usage: " + CHANGE_EMAIL.getCommand() + " <nick>-<newmail>");

        // Valida que el nick no este vacio
        String nick = parts[0].trim();
        String email = parts[1].trim();

        if (nick.isEmpty()) showError("Incorrect nick, must be a non-empty username.");
        if (email.isEmpty()) showError("Incorrect email, must be a non-empty username.");

        changeEmail(nick, email);

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
