package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChangeDescription;

public class ChangeDescriptionCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/desc <description>"); // TODO Podria verificarse en el proceso de comandos ya que la mayoria de comandos tiene argumentos
        /* Me parece que si en la descripcion hay espacios al principio o al final, el servidor los elimina, por que desde aca, el
         * argumento en crudo (getArgumentsRaw()) permite los espacios. Ademas, el servidor tambien valida si la descripcion tiene
         * caracteres invalidos, lo cual este diseño es bastente inconsistente. */
        writeChangeDescription(context.getArgumentsRaw());
    }

}
