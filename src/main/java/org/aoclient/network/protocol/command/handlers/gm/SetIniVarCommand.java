package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetIniVar;

public class SetIniVarCommand extends BaseCommandHandler {

    private static final String USAGE = "/setinivar <llave> <clave> <valor>";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, USAGE);

        String llave = context.getArgument(0);
        requireString(context, 0, "llave");

        String clave = context.getArgument(1);
        requireString(context, 1, "clave");

        String valor = context.getArgument(2);
        requireString(context, 2, "valor");

        // Reemplaza "+" por espacios en el valor (funcionalidad original)
        valor = valor.replace("+", " ");

        writeSetIniVar(llave, clave, valor);
    }

}
