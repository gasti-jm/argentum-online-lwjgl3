package org.aoclient.network.protocol.command;

import java.util.function.Supplier;

/**
 * Clase que representa la definicion de un comando en el sistema. Contiene la informacion necesaria para identificar, clasificar
 * y ejecutar un comando especifico.
 * <p>
 * Cada comando esta compuesto por un nombre, una categoria, una descripcion y una funcion (factory) que genera un handler de
 * comandos. Este handler es el encargado de implementar la logica del comando.
 * <p>
 * Adicionalmente, la clase proporciona metodos estaticos para facilitar la creacion de comandos predefinidos, organizados por
 * categorias comunes como BASIC, GUILD, PARTY o GM. Estos metodos simplifican la configuracion y el registro de comandos en el
 * sistema.
 */

public record CommandDefinition(
        String name,
        Supplier<CommandHandler> factory,
        CommandCategory category,
        String description
) {

    /**
     * Crea una definicion de comando dentro de la categoria {@code BASIC}.
     *
     * @param name        nombre del comando
     * @param factory     funcion que proporciona el {@link CommandHandler} encargado de ejecutar la logica del comando
     * @param description descripcion del comando
     * @return una instancia de {@link CommandDefinition} que representa el comando configurado
     */
    public static CommandDefinition basic(String name, Supplier<CommandHandler> factory, String description) {
        return new CommandDefinition(name, factory, CommandCategory.BASIC, description);
    }

    /**
     * Crea una definicion de comando dentro de la categoria {@code GM}.
     */
    public static CommandDefinition gm(String name, Supplier<CommandHandler> factory, String description) {
        return new CommandDefinition(name, factory, CommandCategory.GM, description);
    }

    /**
     * Crea una definicion de comando dentro de la categoria {@code GUILD}.
     */
    public static CommandDefinition guild(String name, Supplier<CommandHandler> factory, String description) {
        return new CommandDefinition(name, factory, CommandCategory.GUILD, description);
    }

    /**
     * Crea una definicion de comando dentro de la categoria {@code PARTY}.
     */
    public static CommandDefinition party(String name, Supplier<CommandHandler> factory, String description) {
        return new CommandDefinition(name, factory, CommandCategory.PARTY, description);
    }

    /**
     * Crea una definicion de comando simple.
     * <p>
     * Este metodo permite crear comandos simples asignandoles un nombre, una accion a ejecutar y una descripcion breve.
     */
    public static CommandDefinition simple(String name, Runnable action, String description) {
        return new CommandDefinition(name, () -> ctx -> action.run(), CommandCategory.BASIC, description);
    }

}
