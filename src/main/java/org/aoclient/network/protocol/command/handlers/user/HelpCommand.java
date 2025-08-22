package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.console.FontStyle;
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
        console.addMsgToConsole("Commands...", FontStyle.REGULAR, new RGBColor(0f, 1f, 0f));

        // Determina los comandos para GM o USER
        List<String> commands = isGM() ? CommandRegistry.getAllCommandNames() : CommandRegistry.getCommandsByCategory(CommandCategory.USER)
                .stream()
                .map(Command::command)
                .sorted()
                .toList();

        String commandsString = String.join(" ", commands);
        console.addMsgToConsole(commandsString, FontStyle.REGULAR, new RGBColor(1f, 1f, 1f));

        console.addMsgToConsole("", FontStyle.REGULAR, new RGBColor());
        console.addMsgToConsole("Type '" + HELP.getCommand() + " <command>' for specific help.", FontStyle.REGULAR, new RGBColor(0f, 1f, 1f));
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

            console.addMsgToConsole("Help for the " + command.command() + " command...", FontStyle.REGULAR, new RGBColor(0f, 1f, 0f));

            // Muestra ayuda linea por linea
            command.getHelp()
                    .lines()
                    .forEach(line -> console.addMsgToConsole(line, FontStyle.REGULAR, new RGBColor(1f, 1f, 1f)));

        } else handleCommandNotFound(commandName);

    }

    private void handleCommandNotFound(String commandName) {
        console.addMsgToConsole("Command '" + commandName + "' not found.", FontStyle.REGULAR, new RGBColor(1f, 0f, 0f));

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
            console.addMsgToConsole("Did you mean: " + suggestions + " ?", FontStyle.REGULAR, new RGBColor(0.6f, 0.6f, 0.6f));

    }

    private boolean isGM() {
        return user.isGM();
    }

}
