package org.aoclient.network.protocol.command.handlers.basic;

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

            console.addMsgToConsole("- " + category.getDescription() + " -", false, false, new RGBColor(1f, 1f, 0f));

            commands
                    // .limit(5) // En caso de que querer mostrar solo los primeros 5 comandos para no saturar la consola
                    .forEach(cmd -> console.addMsgToConsole(cmd.name() + " - " + cmd.description(), false, false, new RGBColor(0.8f, 0.8f, 0.8f)));

            // Muestra los comandos faltantes en caso de limitar la cantidad a mostrar en consola
            /* if (commands.size() > 5) console.addMsgToConsole("...and " + (commands.size() - 5) + " more", false, false, new RGBColor(0.6f, 0.6f, 0.6f)); */

            console.addMsgToConsole("", false, false, new RGBColor());

        }

        console.addMsgToConsole("Type '" + HELP.getCommand() + " <command>' for specific help", false, false, new RGBColor(0f, 1f, 1f));
    }

    private void showCommandHelp(String commandName) {
        // Agrega / si no lo tiene
        if (!commandName.startsWith("/")) commandName = "/" + commandName;

        Optional<Command> commandInfo = CommandRegistry.getCommandInfo(commandName);

        if (commandInfo.isPresent()) {
            Command cmd = commandInfo.get();

            console.addMsgToConsole("[COMMAND HELP]", false, false, new RGBColor(0f, 1f, 0f));
            console.addMsgToConsole("Command: " + cmd.name(), false, false, new RGBColor(1f, 1f, 0f));
            console.addMsgToConsole("Category: " + cmd.category().getDescription(), false, false, new RGBColor(0.8f, 0.8f, 1f));
            console.addMsgToConsole("Description: " + cmd.description(), false, false, new RGBColor(1f, 1f, 1f));

            // Informacion adicional segun la categoria
            addCategorySpecificHelp(cmd);

        } else {
            console.addMsgToConsole("Command '" + commandName + "' not found!", false, false, new RGBColor(1f, 0f, 0f));
            // Sugiere comandos similares
            suggestSimilarCommands(commandName);
        }
    }

    private void addCategorySpecificHelp(Command cmd) {
        switch (cmd.category()) {
            case GM -> console.addMsgToConsole("Requires GM privileges", false, false, new RGBColor(1f, 0.5f, 0f));
            case GUILD -> console.addMsgToConsole("Guild members only", false, false, new RGBColor(0.5f, 0f, 1f));
            case PARTY -> console.addMsgToConsole("Party members only", false, false, new RGBColor(0f, 0.8f, 0.8f));
        }
    }

    private void suggestSimilarCommands(String commandName) {
        List<String> suggestions = CommandRegistry.getAllCommandNames().stream()
                .filter(cmd -> cmd.toLowerCase().contains(commandName.toLowerCase().replace("/", "")))
                .limit(3)
                .toList();

        if (!suggestions.isEmpty()) {
            console.addMsgToConsole("Did you mean:", false, false, new RGBColor(0.8f, 0.8f, 0f));
            suggestions.forEach(suggestion -> console.addMsgToConsole("  " + suggestion, false, false, new RGBColor(0.6f, 0.6f, 0.6f))
            );
        }
    }

}
