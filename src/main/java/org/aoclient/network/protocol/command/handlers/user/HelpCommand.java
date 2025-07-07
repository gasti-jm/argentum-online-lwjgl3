package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.game.Console;
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
 * TODO Solo mostrar los comandos de GM a los GM
 */

public class HelpCommand extends BaseCommandHandler {

    private final Console console = Console.INSTANCE;

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
        console.addMsgToConsole("[AVAILABLE COMMANDS]", false, false, new RGBColor(0f, 1f, 0f));

        // Muestra comandos por categoria
        for (CommandCategory category : CommandCategory.values()) {
            List<Command> commands = CommandRegistry.getCommandsByCategory(category);
            console.addMsgToConsole("- " + category.name() + " -", false, false, new RGBColor(1f, 1f, 0f));
            commands.forEach(cmd -> console.addMsgToConsole(cmd.name(), false, false, new RGBColor(0.8f, 0.8f, 0.8f)));
            console.addMsgToConsole("", false, false, new RGBColor());
        }

        console.addMsgToConsole("Type '" + HELP.getCommand() + " <command>' for specific help.", false, false, new RGBColor(0f, 1f, 1f));
    }

    private void showCommandHelp(String commandName) {
        // Agrega / si no lo tiene
        if (!commandName.startsWith("/")) commandName = "/" + commandName;

        Optional<Command> commandInfo = CommandRegistry.getCommandInfo(commandName);

        if (commandInfo.isPresent()) {
            Command cmd = commandInfo.get();

            console.addMsgToConsole("[COMMAND HELP]", false, false, new RGBColor(0f, 1f, 0f));
            console.addMsgToConsole("Command: " + cmd.name(), false, false, new RGBColor(1f, 1f, 0f));
            console.addMsgToConsole("Description: " + cmd.description(), false, false, new RGBColor(1f, 1f, 1f));
            console.addMsgToConsole("Category: " + cmd.category().name(), false, false, new RGBColor(0.8f, 0.8f, 1f));

        } else {
            console.addMsgToConsole("Command '" + commandName + "' not found!", false, false, new RGBColor(1f, 0f, 0f));
            // Sugiere comandos similares
            suggestSimilarCommands(commandName);
        }
    }

    private void suggestSimilarCommands(String commandName) {
        List<String> suggestions = CommandRegistry.getAllCommandNames().stream()
                .filter(cmd -> cmd.toLowerCase().contains(commandName.toLowerCase().replace("/", "")))
                .limit(5)
                .toList();

        if (!suggestions.isEmpty()) {
            console.addMsgToConsole("Did you mean:", false, false, new RGBColor(0.8f, 0.8f, 0f));
            suggestions.forEach(suggestion -> console.addMsgToConsole("  " + suggestion, false, false, new RGBColor(0.6f, 0.6f, 0.6f)));
        }
    }

}
