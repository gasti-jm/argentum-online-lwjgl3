package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para crear y registrar comandos de manera sencilla utilizando un patron de construccion fluido (builder pattern). Los
 * comandos creados pueden ser asociados a acciones que seran ejecutadas cuando el comando sea procesado.
 * <p>
 * Esta clase define un builder interno, {@link SimpleCommand.SimpleCommandBuilder}, que permite:
 * <ul>
 *  <li>Definir un comando mediante el metodo {@link SimpleCommand.SimpleCommandBuilder#command(String)}.
 *  <li>Asociar una accion al comando con el metodo {@link SimpleCommand.SimpleCommandBuilder#action(Runnable)}.
 *  <li>Asociar una accion condicionada con {@link SimpleCommand.SimpleCommandBuilder#actionWithDeath(Runnable)},
 *      la cual verifica si una condicion particular (en este caso, si el usuario esta "muerto") es cumplida
 *      antes de ejecutar la accion.
 *  <li>Registrar los comandos creados en un mapa externo con el metodo {@link SimpleCommand.SimpleCommandBuilder#registerTo(Map)}.
 * </ul>
 * <p>
 * El builder facilita el registro de comandos y asegura que las acciones asociadas sean ejecutadas en el contexto correcto,
 * incluyendo el manejo de excepciones en caso de condiciones especiales.
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

        public SimpleCommandBuilder actionWithDeath(Runnable action) {
            tempCommands.put(command, ctx -> {
                if (User.INSTANCE.isDead()) throw new CommandException("You are dead!");
                action.run();
            });
            return this;
        }

        public void registerTo(Map<String, CommandHandler> commands) {
            commands.putAll(tempCommands);
        }

    }

}
