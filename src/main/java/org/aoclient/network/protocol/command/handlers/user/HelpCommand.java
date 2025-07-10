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

import static org.aoclient.network.protocol.command.metadata.GameCommand.HELP;

/**
 * Representa un handler para el comando {@code /?}. Su funcion principal es proporcionar informacion de ayuda general o
 * especifica segun los argumentos proporcionados por el usuario.
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Mostrar una lista general de comandos disponibles, organizados por categoria.
 * <li>Ofrecer ayuda detallada para un comando especifico, incluyendo descripcion, categoria y requisitos adicionales segun el
 * tipo de comando.
 * <li>Sugerir comandos similares en caso de que el comando solicitado no exista.
 * </ul>
 * <p>
 * Este comando es util para los usuarios que necesitan informacion sobre como usar otros comandos dentro del sistema,
 * especialmente en contextos con muchos comandos disponibles.
 * <p>
 * IMPORTANTE: Solo los GMs podran ver los comandos de GM.
 * <p>
 * TODO Mostrar los argumentos necesarios cuando se hace un /? teleport por ejemplo
 */

public class HelpCommand extends BaseCommandHandler {

    private final Console console = Console.INSTANCE;
    private final User user = User.INSTANCE;

    @Override
    public void handle(CommandContext commandContext) throws CommandException {

        // writeHelp(); // Obtiene informacion obsoleta del servidor de VB6

        // Muestra ayuda general
        if (!commandContext.hasArguments()) showGeneralHelp();
        else {
            // Muestra ayuda especifica de un comando
            String commandName = commandContext.getArgument(0);
            showCommandHelp(commandName);
        }
    }

    private void showGeneralHelp() {
        console.addMsgToConsole("Commands...", false, false, new RGBColor(0f, 1f, 0f));

        // Determina los comandos para GM o USER
        List<String> commands;
        if (isGM()) commands = CommandRegistry.getAllCommandNames();
        else
            commands = CommandRegistry.getCommandsByCategory(CommandCategory.USER).stream()
                    .map(Command::name)
                    .sorted()
                    .toList();

        // Formatea comandos con saltos de linea automaticos
        formatCommandsWithLineBreaks(commands);

        console.addMsgToConsole("", false, false, new RGBColor());
        console.addMsgToConsole("Type '" + HELP.getCommand() + " <command>' for specific help.", false, false, new RGBColor(0f, 1f, 1f));
    }


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


    private void showCommandHelp(String commandName) {
        // Agrega / si no lo tiene
        if (!commandName.startsWith("/")) commandName = "/" + commandName;

        Optional<Command> commandInfo = CommandRegistry.getCommandInfo(commandName);

        if (commandInfo.isPresent()) {
            Command cmd = commandInfo.get();

            // Verifica si el comando es GM y el usuario no es GM
            if (cmd.category() == CommandCategory.GM && !isGM()) {
                console.addMsgToConsole("Command '" + commandName + "' not found!", false, false, new RGBColor(1f, 0f, 0f));
                suggestSimilarCommands(commandName);
                return;
            }

            console.addMsgToConsole("Command: " + cmd.name(), false, false, new RGBColor(1f, 1f, 0f));
            console.addMsgToConsole("Description: " + cmd.description(), false, false, new RGBColor(1f, 1f, 1f));

        } else {
            console.addMsgToConsole("Command '" + commandName + "' not found!", false, false, new RGBColor(1f, 0f, 0f));
            // Sugiere comandos similares
            suggestSimilarCommands(commandName);
        }
    }

    private void suggestSimilarCommands(String commandName) {
        List<String> suggestions = CommandRegistry.getAllCommandNames().stream()
                .filter(cmd -> {
                    // Filtrar comandos GM si el usuario no es GM
                    Optional<Command> commandInfo = CommandRegistry.getCommandInfo(cmd);
                    if (commandInfo.isPresent() && commandInfo.get().category() == CommandCategory.GM && !isGM())
                        return false;
                    return cmd.toLowerCase().contains(commandName.toLowerCase().replace("/", ""));
                })
                .limit(5)
                .toList();

        if (!suggestions.isEmpty()) {
            console.addMsgToConsole("Did you mean:", false, false, new RGBColor(0.8f, 0.8f, 0f));
            suggestions.forEach(suggestion -> console.addMsgToConsole("  " + suggestion, false, false, new RGBColor(0.6f, 0.6f, 0.6f)));
        }
    }

    private boolean isGM() {
        return user.isGM();
    }

}
