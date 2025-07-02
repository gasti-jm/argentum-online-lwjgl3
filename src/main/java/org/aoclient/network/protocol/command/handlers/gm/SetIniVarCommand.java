package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSetIniVar;

@Command("/setinivar")
@SuppressWarnings("unused")
public class SetIniVarCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 3, "/setinivar <llave> <clave> <valor>");
        requireString(textContext, 0, "llave");
        requireString(textContext, 1, "clave");
        requireString(textContext, 2, "valor");
        String llave = textContext.getArgument(0);
        String clave = textContext.getArgument(1);
        String valor = textContext.getArgument(2);
        // Reemplaza "+" por espacios en el valor (funcionalidad original)
        valor = valor.replace("+", " ");
        writeSetIniVar(llave, clave, valor);
    }

}
