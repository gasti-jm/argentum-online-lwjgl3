package org.aoclient.network.protocol.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Facilita la creacion y configuracion de comandos a traves de un constructor interno llamado {@code SimpleCommandBuilder}.
 * <p>
 * Un comando tipicamente esta compuesto por un nombre (como identificador unico) y una accion (logica a ejecutar cuando el
 * comando es activado). Los comandos se pueden asociar con gestores (handlers) de comandos de forma dinamica.
 * <p>
 * Para crear un comando se utiliza el metodo estatico {@code builder()}, que devuelva una instancia de
 * {@code SimpleCommandBuilder}. Este constructor interno permite especificar el nombre del comando, definir la accion que se
 * ejecutara al invocar el comando y finalmente registrar este comando en una coleccion de comandos existente.
 * <p>
 * Ejemplo de flujo simplificado al definir un comando:
 * <ol>
 *  <li>Iniciar a traves del metodo {@code builder()}.
 *  <li>Establecer el nombre del comando usando el metodo {@code command(String name)}.
 *  <li>Definir la accion a ejecutar con el metodo {@code action(Runnable action)}.
 *  <li>Registrar el comando resultante en un mapa de comandos utilizando {@code registerTo(Map)}.
 * </ol>
 */

public class SimpleCommand {

    public static SimpleCommandBuilder builder() {
        return new SimpleCommandBuilder();
    }

    public static class SimpleCommandBuilder {
        private final Map<String, CommandHandler> tempCommands = new HashMap<>();
        private String command;

        public SimpleCommandBuilder command(String command) {
            this.command = command;
            return this;
        }

        public SimpleCommandBuilder action(Runnable action) {
            tempCommands.put(command, ctx -> action.run());
            return this;
        }

        public void registerTo(Map<String, CommandHandler> commands) {
            commands.putAll(tempCommands);
        }

    }

}
