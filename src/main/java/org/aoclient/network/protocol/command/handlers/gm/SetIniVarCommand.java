package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.setIniVar;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SETINIVAR;

public class SetIniVarCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, getCommandUsage(SETINIVAR));
        requireString(commandContext, 0, "llave");
        requireString(commandContext, 1, "clave");
        requireString(commandContext, 2, "valor");
        String llave = commandContext.getArgument(0);
        String clave = commandContext.getArgument(1);
        String valor = commandContext.getArgument(2);
        // Reemplaza "+" por espacios en el valor (funcionalidad original)
        valor = valor.replace("+", " ");
        setIniVar(llave, clave, valor);
    }

}
