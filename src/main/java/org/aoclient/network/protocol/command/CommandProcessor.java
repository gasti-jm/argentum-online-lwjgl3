package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;

import java.util.Map;

/**
 * Procesador de comandos para el sistema basado en comandos.
 * <p>
 * Su funcion principal es manejar comandos ingresados por el usuario, identificarlos y ejecutar la logica asociada, utilizando un
 * mapeo entre nombres de comandos y handlers que implementan estas acciones.
 * <p>
 * Este procesador ayuda a mantener organizada la logica de los comandos al delegar su ejecucion en objetos que encapsulan el
 * comportamiento especifico de cada comando.
 *
 * TODO Autocompletado de comandos
 */

public enum CommandProcessor {

    INSTANCE;

    /** Mapea comandos del juego a sus handlers correspondientes para su procesamiento. */
    private final Map<String, CommandHandler> commands;

    CommandProcessor() {
        commands = CommandRegistry.buildCommandMap();
    }

    /**
     * Procesa un comando ingresado como cadena de texto, verificando si es un comando valido y ejecutandolo si corresponde.
     *
     * @param command Cadena de texto que representa el comando completo ingresado. Esta cadena debe incluir el nombre del comando
     *                (por ejemplo, "/telep") y puede incluir argumentos adicionales.
     */
    public void process(String command) {
        CommandContext commandContext = CommandContext.parse(command);
        if (commandContext.isCommand()) execute(commandContext);
    }

    /**
     * Ejecuta el handler asociado al comando especificado en el contexto.
     * <p>
     * Si el comando existe y tiene un handler registrado, se intenta ejecutar su logica a traves del metodo {@code handle}. En
     * caso de que el handler arroje una excepcion, se captura y muestra un mensaje de error en la consola. Si el comando no es
     * reconocido, se muestra un mensaje de comando desconocido.
     *
     * @param commandContext objeto que contiene informacion sobre el comando a ejecutar, incluyendo el nombre del comando y sus
     *                       argumentos asociados
     */
    private void execute(CommandContext commandContext) {
        CommandHandler handler = commands.get(commandContext.command());
        if (handler != null) {
            try {
                handler.handle(commandContext);
            } catch (CommandException e) {
                Console.INSTANCE.addMsgToConsole(e.getMessage(), false, true, new RGBColor());
            }
        } else
            Console.INSTANCE.addMsgToConsole("'" + commandContext.command() + "' is not a game command. See '/help'.", false, true, new RGBColor());
    }

}
