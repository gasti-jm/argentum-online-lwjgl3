package org.aoclient.network.protocol.command.core;

/**
 * Interface que define una estructura basica para el manejo de comandos en el sistema. Los comandos son procesados a traves del
 * metodo {@link #handle(CommandContext)}, donde se implementa la logica especifica de cada comando.
 * <p>
 * Tambien proporciona un metodo por defecto, {@link #showError(String)}, que permite gestionar errores en los comandos, mostrando
 * un mensaje en consola y lanzando una excepcion.
 */

public interface CommandHandler {

    void handle(CommandContext commandContext) throws CommandException;

    default void showError(String message) throws CommandException {
        throw new CommandException(message);
    }

}
