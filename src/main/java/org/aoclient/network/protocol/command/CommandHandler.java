package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;

/**
 * Interface que define una estructura basica para el manejo de comandos en el sistema. Los comandos son procesados a traves del
 * metodo {@link #handle(TextContext)}, donde se implementa la logica especifica de cada comando.
 * <p>
 * Tambien proporciona un metodo por defecto, {@link #showError(String)}, que permite gestionar errores en los comandos, mostrando
 * un mensaje en consola y lanzando una excepcion.
 */

public interface CommandHandler {

    void handle(TextContext textContext) throws CommandException;

    default void showError(String message) throws CommandException {
        Console.INSTANCE.addMsgToConsole(message, false, true, new RGBColor());
        throw new CommandException(message);
    }

}
