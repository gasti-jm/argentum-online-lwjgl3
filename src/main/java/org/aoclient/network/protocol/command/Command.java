package org.aoclient.network.protocol.command;

import java.util.function.Supplier;

/**
 * Clase que representa un comando en el sistema. Un comando incluye su nombre, una fabrica para crear un handler que ejecuta su
 * logica, una categoria para clasificarlo y una descripcion.
 * <p>
 * Los comandos pueden ser clasificados en diferentes categorias segun su proposito, como {@code BASIC}, {@code GM},
 * {@code GUILD}, o {@code PARTY}. Cada comando puede ejecutar una accion determinada ya sea mediante un handler especifico (por
 * medio de un {@code Supplier} que proporciona un {@code CommandHandler}) o una accion simple representada por un
 * {@code Runnable}.
 */

public record Command(
        String name,
        Supplier<CommandHandler> factory,
        CommandCategory category,
        String description
) {

    /**
     * Crea un comando de tipo {@code BASIC} con logica especificada.
     *
     * @param name        nombre del comando
     * @param factory     funcion que proporciona el {@link CommandHandler} encargado de ejecutar la logica del comando
     * @param description descripcion del comando
     * @return una instancia de {@link Command} que representa el comando configurado
     */
    public static Command basic(String name, Supplier<CommandHandler> factory, String description) {
        return new Command(name, factory, CommandCategory.BASIC, description);
    }

    /**
     * Crea un comando de tipo {@code BASIC} con accion simple especificada.
     *
     * @param name        nombre del comando
     * @param action      accion simple
     * @param description descripcion del comando
     * @return una instancia de {@link Command} que representa el comando configurado
     */
    public static Command basic(String name, Runnable action, String description) {
        return new Command(name, () -> ctx -> action.run(), CommandCategory.BASIC, description);
    }

    public static Command gm(String name, Supplier<CommandHandler> factory, String description) {
        return new Command(name, factory, CommandCategory.GM, description);
    }

    public static Command gm(String name, Runnable action, String description) {
        return new Command(name, () -> ctx -> action.run(), CommandCategory.GM, description);
    }


    public static Command guild(String name, Supplier<CommandHandler> factory, String description) {
        return new Command(name, factory, CommandCategory.GUILD, description);
    }

    public static Command guild(String name, Runnable action, String description) {
        return new Command(name, () -> ctx -> action.run(), CommandCategory.GUILD, description);
    }


    public static Command party(String name, Supplier<CommandHandler> factory, String description) {
        return new Command(name, factory, CommandCategory.PARTY, description);
    }

    public static Command party(String name, Runnable action, String description) {
        return new Command(name, () -> ctx -> action.run(), CommandCategory.PARTY, description);
    }

}
