package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetIniVar;

@Command("/setinivar")
@SuppressWarnings("unused")
public class SetIniVarCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 3, "/setinivar <llave> <clave> <valor>");
        requireString(context, 0, "llave");
        requireString(context, 1, "clave");
        requireString(context, 2, "valor");
        String llave = context.getArgument(0);
        String clave = context.getArgument(1);
        String valor = context.getArgument(2);
        // Reemplaza "+" por espacios en el valor (funcionalidad original)
        valor = valor.replace("+", " ");
        writeSetIniVar(llave, clave, valor);
    }

}
