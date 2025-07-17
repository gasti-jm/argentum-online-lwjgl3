package org.aoclient.network.protocol.command.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.protocol.command.core.Command;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.core.CommandHandler;
import org.aoclient.network.protocol.command.execution.CommandRegistry;
import org.aoclient.network.protocol.command.metadata.GameCommand;

/**
 * Clase abstracta que proporciona una implementacion base para manejar comandos, estableciendo validaciones comunes y utilidades
 * necesarias para procesar los argumentos de los comandos. Esta clase es parte del sistema de comandos y debe extenderse por
 * clases concretas que definan logica especifica de manejo de comandos.
 * <p>
 * Proporciona varios metodos de utilidad para verificar argumentos, cadenas de texto, enteros y patrones de expresion regular.
 * Estos metodos estan diseñados para garantizar que los argumentos de un comando cumplan ciertos criterios antes de procesarlos.
 * <p>
 * La clase tambien incluye miembros de consola y usuario para facilitar el acceso a estos recursos en la implementacion de
 * comandos concretos.
 * <p>
 * Los argumentos que se indican entre "<>" son obligatorios y entre "[]" son opcionales.
 * <p>
 * TODO Mostrar mensaje para los comandos que no necesitan argumentos
 */

public abstract class BaseCommandHandler implements CommandHandler {

    /**
     * Patron de expresion regular que coincide con letras minusculas/mayusculas, numeros, espacios, puntos, comas y signos de
     * interrogacion. El <b>+</b> significa "uno o mas" caracteres que coincidan con la clase de caracteres, permitiendo cadenas
     * de cualquier longitud.
     */
    protected final String REGEX = "[a-zA-Z0-9 .,¿?]+";

    /** Indica un numero ilimitado de argumentos cuando se pasa como parametro a los metodos de validacion de argumentos. */
    protected final int UNLIMITED_ARGUMENTS = -1;
    protected User user = User.INSTANCE;

    /**
     * Obtiene el uso/sintaxis del comando basado en el GameCommand.
     */
    protected static String getCommandUsage(GameCommand gameCommand) {
        return CommandRegistry.getCommandInfo(gameCommand.getCommand())
                .map(Command::getUsage)
                .orElse(gameCommand.getCommand() + " <args>");
    }

    protected void requireArguments(CommandContext commandContext, int count, String usage) throws CommandException {
        if (!commandContext.hasArguments()) showError("Missing arguments. " + usage);
        // Evita que se verifique la cantidad de argumentos de mas de un argumento como la descripcion del bug por ejemplo
        if (count == UNLIMITED_ARGUMENTS) return;
        if (commandContext.getArgumentCount() != count)
            showError("The command requires " + count + " argument" + (count > 1 ? "s" : "") + ". Usage: " + usage);
    }

    protected void requireString(CommandContext commandContext, int index, String argument) throws CommandException {
        String string = commandContext.getArgument(index);
        if (string.isEmpty() || string.trim().isEmpty()) showError("The " + argument + " cannot be empty.");
    }

    protected void requireValidString(CommandContext commandContext, String argument, String pattern) throws CommandException {
        int maxLength = 100;
        String string = commandContext.argumentsRaw().trim();
        if (!string.matches(pattern))
            showError("The " + argument + " contains invalid characters. Valid characters: " + pattern.substring(0, pattern.length() - 1));
        if (string.length() > maxLength) showError("The " + argument + " cannot exceed " + maxLength + " characters.");
    }

    protected void requireInteger(CommandContext commandContext, int index, String argument) throws CommandException {
        String value = commandContext.getArgument(index);
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) showError("The " + argument + " must be greater than 0.");
        } catch (NumberFormatException e) {
            showError("The " + argument + " must be a number between " + 1 + " and " + Integer.MAX_VALUE + ".");
        }
    }

    protected void requireShort(CommandContext commandContext, int index, String argument) throws CommandException {
        String value = commandContext.getArgument(index);
        try {
            int parsedValue = Short.parseShort(value);
            if (parsedValue <= 0) showError("The " + argument + " must be greater than 0.");
        } catch (NumberFormatException e) {
            showError("The " + argument + " must be a number between " + 1 + " and " + Short.MAX_VALUE + ".");
        }
    }

}
