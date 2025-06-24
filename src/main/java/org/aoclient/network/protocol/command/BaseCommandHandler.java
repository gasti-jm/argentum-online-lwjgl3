package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;

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
 */

public abstract class BaseCommandHandler implements CommandHandler {

    /**
     * Patron de expresion regular que coincide con letras minusculas/mayusculas, numeros, espacios, puntos, comas y signos de
     * interrogacion. El <b>+</b> significa "uno o mas" caracteres que coincidan con la clase de caracteres, permitiendo cadenas
     * de cualquier longitud.
     */
    protected static final String REGEX = "[a-zA-Z0-9 .,¿?]+";
    protected User user = User.INSTANCE;
    protected Console console = Console.INSTANCE;

    protected void requireArguments(CommandContext context, int count, String usage) throws CommandException {
        if (!context.hasArguments()) showError("Missing arguments. Usage: " + usage);
        // Evita que se verifique la cantidad de argumentos de mas de un argumento como la descripcion del bug por ejemplo
        if (count == -1) return;
        if (context.getArgumentCount() != count)
            showError("The command requires " + count + " argument" + (count > 1 ? "s" : "") + ". Usage: " + usage);
    }

    protected void requireString(CommandContext context, int index, String argument) throws CommandException {
        String string = context.getArgument(index);
        if (string.isEmpty() || string.trim().isEmpty()) showError("The " + argument + " cannot be empty.");
    }

    protected void requireValidString(CommandContext context, String argument, String pattern) throws CommandException {
        int maxLength = 100;
        String string = context.getArgumentsRaw().trim();
        if (!string.matches(pattern))
            showError("The " + argument + " contains invalid characters. Valid characters: " + pattern.substring(0, pattern.length() - 1));
        if (string.length() > maxLength) showError("The " + argument + " cannot exceed " + maxLength + " characters.");
    }

    protected void requireInteger(CommandContext context, int index, String argument) throws CommandException {
        String value = context.getArgument(index);
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) showError("The " + argument + " must be greater than 0.");
        } catch (NumberFormatException e) {
            showError("The " + argument + " must be a number between " + 1 + " and " + Integer.MAX_VALUE + ".");
        }
    }

    protected void requireShort(CommandContext context, int index, String argument) throws CommandException {
        String value = context.getArgument(index);
        try {
            int parsedValue = Short.parseShort(value);
            if (parsedValue <= 0) showError("The " + argument + " must be greater than 0.");
        } catch (NumberFormatException e) {
            showError("The " + argument + " must be a number between " + 1 + " and " + Short.MAX_VALUE + ".");
        }
    }

}
