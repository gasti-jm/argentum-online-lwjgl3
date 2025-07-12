package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.renderer.FontRenderer;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.core.Command;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.execution.CommandRegistry;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.metadata.CommandCategory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aoclient.network.protocol.command.metadata.GameCommand.HELP;

/**
 * Proporciona ayuda sobre los comandos disponibles.
 * <p>
 * Caracteristicas principales:
 * <ul>
 *  <li>Si no se proporcionan argumentos, muestra una lista general de los comandos disponibles, dependiendo de si el usuario es
 * GM (Game Master) o no.
 *  <li>Si se proporciona el nombre de un comando como argumento, muestra ayuda detallada para dicho comando, incluyendo una
 * descripcion y su funcionalidad.
 *  <li>Sugiere comandos similares en caso de que el nombre del comando especificado no exista.
 * </ul>
 * Uso:
 * <ul>
 *  <li>Para mostrar todos los comandos: {@code /?}
 *  <li>Para obtener informacion de un comando especifico: {@code /? teleport}
 * </ul>
 */

public class HelpCommand extends BaseCommandHandler {

    private final Console console = Console.INSTANCE;
    private final User user = User.INSTANCE;

    @Override
    public void handle(CommandContext commandContext) throws CommandException {

        // writeHelp(); // Obtiene informacion obsoleta del servidor de VB6

        if (!commandContext.hasArguments()) showGeneralHelp();
        else {
            String commandName = commandContext.getArgument(0);
            showCommandHelp(commandName);
        }
    }

    private void showGeneralHelp() {
        console.addMsgToConsole("Commands...", false, false, new RGBColor(0f, 1f, 0f));

        // Determina los comandos para GM o USER
        List<String> commands = isGM() ? CommandRegistry.getAllCommandNames() : CommandRegistry.getCommandsByCategory(CommandCategory.USER)
                .stream()
                .map(Command::command)
                .sorted()
                .toList();

        // Formatea los comandos con saltos de linea automaticos
        formatCommandsWithLineBreaks(commands);

        console.addMsgToConsole("", false, false, new RGBColor());
        console.addMsgToConsole("Type '" + HELP.getCommand() + " <command>' for specific help.", false, false, new RGBColor(0f, 1f, 1f));
    }

    private void showCommandHelp(String commandName) {
        if (!commandName.startsWith("/")) commandName = "/" + commandName;

        Optional<Command> commandInfo = CommandRegistry.getCommandInfo(commandName);

        if (commandInfo.isPresent()) {
            Command command = commandInfo.get();

            // Verifica si el comando es GM y el usuario no es GM
            if (command.category() == CommandCategory.GM && !isGM()) {
                handleCommandNotFound(commandName);
                return;
            }

            console.addMsgToConsole("Help for the " + command.command() + " command...", false, false, new RGBColor(0f, 1f, 0f));

            // Muestra ayuda linea por linea
            command.getHelp()
                    .lines()
                    .forEach(line -> console.addMsgToConsole(line, false, false, new RGBColor(1f, 1f, 1f)));

        } else handleCommandNotFound(commandName);

    }

    private void handleCommandNotFound(String commandName) {
        console.addMsgToConsole("Command '" + commandName + "' not found.", false, false, new RGBColor(1f, 0f, 0f));

        // Sugiere comandos similares usando CommandRegistry
        var suggestions = CommandRegistry.getAllCommandNames()
                .stream()
                .filter(cmd -> {
                    // Filtrar comandos GM si el usuario no es GM
                    Optional<Command> commandInfo = CommandRegistry.getCommandInfo(cmd);
                    if (commandInfo.isPresent() && commandInfo.get().category() == CommandCategory.GM && !isGM()) return false;
                    return cmd.toLowerCase().contains(commandName.toLowerCase().replace("/", ""));
                })
                .limit(4)
                .collect(Collectors.joining(", "));

        if (!suggestions.isEmpty())
            console.addMsgToConsole("Did you mean: " + suggestions + " ?", false, false, new RGBColor(0.6f, 0.6f, 0.6f));

    }

    /**
     * Formatea una lista de comandos y los imprime en la consola respetando un ancho maximo por linea.
     * <p>
     * Esta funcion toma una lista de cadenas que representan comandos, los organiza agregando saltos de linea donde sea necesario
     * para evitar exceder el ancho maximo establecido para la consola. Cada linea formada se imprime en la consola con un color
     * gris claro.
     *
     * @param commands Lista de cadenas que representan los comandos a formatear e imprimir. Si esta lista esta vacia, el metodo
     *                 no realiza ninguna accion.
     */
    private void formatCommandsWithLineBreaks(List<String> commands) {
        if (commands.isEmpty()) return;

        StringBuilder currentLine = new StringBuilder();

        // Ancho de consola con 10 pixeles de margen
        int consoleWidth = Console.CONSOLE_WIDTH - 10;

        for (String command : commands) {

            // Construye el texto del comando actual, agregandolo a la linea actual si existe
            String text = currentLine.isEmpty() ? command : currentLine + " " + command;

            // Calcula el ancho del texto
            int textWidth = FontRenderer.getTextWidth(text, false);

            // Si excede el ancho de la consola, imprime la linea actual y empieza una nueva
            if (textWidth > consoleWidth && !currentLine.isEmpty()) {
                console.addMsgToConsole(currentLine.toString(), false, false, new RGBColor(0.8f, 0.8f, 0.8f));
                currentLine = new StringBuilder(command);
            } else {
                // Si la linea actual no esta vacia, entonces agrega un espacio
                if (!currentLine.isEmpty()) currentLine.append(" ");
                // Agrega el comando a la linea actual
                currentLine.append(command);
            }
        }

        // Imprime la ultima linea si no esta vacia
        if (!currentLine.isEmpty()) console.addMsgToConsole(currentLine.toString(), false, false, new RGBColor(0.8f, 0.8f, 0.8f));

    }

    private boolean isGM() {
        return user.isGM();
    }

}
