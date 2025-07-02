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
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, "/setinivar <llave> <clave> <valor>");
        requireString(commandContext, 0, "llave");
        requireString(commandContext, 1, "clave");
        requireString(commandContext, 2, "valor");
        String llave = commandContext.getArgument(0);
        String clave = commandContext.getArgument(1);
        String valor = commandContext.getArgument(2);
        // Reemplaza "+" por espacios en el valor (funcionalidad original)
        valor = valor.replace("+", " ");
        writeSetIniVar(llave, clave, valor);
    }

}
