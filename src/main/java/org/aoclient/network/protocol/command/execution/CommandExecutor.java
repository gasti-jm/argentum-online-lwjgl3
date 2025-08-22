package org.aoclient.network.protocol.command.execution;

import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.core.CommandHandler;
import org.aoclient.network.protocol.command.metadata.CommandCategory;

import java.util.Map;

/**
 * Maneja la ejecucion de comandos en el juego. Utiliza un sistema de mapeo para asociar cadenas de texto (comandos) con sus
 * handlers correspondientes, responsables de ejecutar la logica especifica de cada comando.
 */

public enum CommandExecutor {

    INSTANCE;

    /** Mapea comandos del juego a sus handlers correspondientes para su procesamiento. */
    private final Map<String, CommandHandler> commands;

    CommandExecutor() {
        commands = CommandRegistry.buildCommandMap();
    }

    /**
     * Ejecuta un comando proporcionado en forma de cadena. La cadena representara un comando ingresado por el usuario, sera
     * analizada y, si es valida, se ejecutara utilizando el handler asociado al comando. En caso de errores, los mensajes
     * correspondientes seran mostrados en consola.
     *
     * @param command La cadena de texto que representa el comando completo a ejecutar. Debe comenzar con "/" para ser reconocido
     *                como un comando valido.
     */
    public void execute(String command) {
        CommandContext commandContext = CommandContext.parse(command);
        if (commandContext.isCommand()) {
            CommandHandler handler = commands.get(commandContext.command());
            if (handler != null) {

                if (isGMCommandWithoutPrivileges(commandContext.command())) {
                    Console.INSTANCE.addMsgToConsole("'" + commandContext.command() + "' is not a game command. See '/?'.", FontStyle.ITALIC, new RGBColor());
                    return;
                }

                try {
                    handler.handle(commandContext);
                } catch (CommandException e) {
                    Console.INSTANCE.addMsgToConsole(e.getMessage(), FontStyle.ITALIC, new RGBColor());
                }

            } else
                Console.INSTANCE.addMsgToConsole("'" + commandContext.command() + "' is not a game command. See '/?'.", FontStyle.ITALIC, new RGBColor());
        }
    }

    /**
     * Verifica si un comando pertenece a la categoria "GM" y si el usuario actual no tiene los privilegios necesarios para
     * ejecutarlo.
     *
     * @param commandName nombre del comando que se desea verificar
     * @return true si el comando es de la categoria "GM" y el usuario no tiene privilegios de GM; false en caso contrario
     */
    private boolean isGMCommandWithoutPrivileges(String commandName) {
        return CommandRegistry.getCommandInfo(commandName)
                .filter(cmd -> cmd.category() == CommandCategory.GM)
                .map(cmd -> !User.INSTANCE.isGM())
                .orElse(false);
    }

}
