package org.aoclient.network.protocol.command.metadata;

/**
 * Enumeracion que representa diferentes categorias de comandos en el sistema. Cada categoria esta asociada con una descripcion
 * que indica su proposito general.
 * <p>
 * Esta clase es util para organizar y clasificar los comandos por su funcionalidad, lo que facilita su gestion y uso dentro de
 * diferentes contextos.
 */

public enum CommandCategory {

    BASIC("Basic commands"),
    GUILD("Guild commands"),
    PARTY("Party commands"),
    GM("GM Commands");

    private final String description;

    CommandCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

