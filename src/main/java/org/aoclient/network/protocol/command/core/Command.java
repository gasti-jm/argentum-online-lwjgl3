package org.aoclient.network.protocol.command.core;

import org.aoclient.network.protocol.command.metadata.CommandCategory;
import org.aoclient.network.protocol.command.metadata.GameCommand;

import java.util.function.Supplier;

/**
 * Clase que representa un comando en el sistema. Un comando incluye nombre, fabrica para crear un handler que ejecuta su logica,
 * descripcion y categoria.
 * <p>
 * Los comandos se clasifican en dos categorias segun su proposito: {@code USER} y {@code GM}. Cada comando puede ejecutar una
 * accion determinada ya sea mediante un handler especifico (por medio de un {@code Supplier} que proporciona un
 * {@code CommandHandler}) o una accion simple representada por un {@code Runnable}.
 */

public record Command(
        String name,
        Supplier<CommandHandler> factory,
        String description,
        CommandCategory category
) {

    /**
     * Crea un comando de usuario con accion simple especificada.
     *
     * @param cmd    comando
     * @param action accion simple
     * @return una instancia de {@link Command} que representa el comando configurado
     */
    public static Command user(GameCommand cmd, Runnable action) {
        return new Command(cmd.getCommand(), () -> ctx -> action.run(), cmd.getDescription(), CommandCategory.USER);
    }

    /**
     * Crea un comando de usuario con logica especificada.
     *
     * @param cmd     comando
     * @param factory funcion que proporciona el {@link CommandHandler} encargado de ejecutar la logica del comando
     * @return una instancia de {@link Command} que representa el comando configurado
     */
    public static Command user(GameCommand cmd, Supplier<CommandHandler> factory) {
        return new Command(cmd.getCommand(), factory, cmd.getDescription(), CommandCategory.USER);
    }

    public static Command gm(GameCommand cmd, Runnable action) {
        return new Command(cmd.getCommand(), () -> ctx -> action.run(), cmd.getDescription(), CommandCategory.GM);
    }

    public static Command gm(GameCommand cmd, Supplier<CommandHandler> factory) {
        return new Command(cmd.getCommand(), factory, cmd.getDescription(), CommandCategory.GM);
    }

}
