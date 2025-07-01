package org.aoclient.network.protocol.command;

import java.util.List;

/**
 * Contexto de comando inmutable que representa un comando parseado. Versión simplificada que mantiene compatibilidad con código
 * existente.
 *
 * TODO TextContext?
 */

public record CommandContext(
        String text,
        String command,
        List<String> arguments, // Para comandos que necesitan argumentos individuales
        String argumentsRaw // Para comandos que necesitan el texto completo
) {

    /** Limite para controlar la cantidad de veces que se divide un comando en sus partes (comando y argumentos). */
    private static final int COMMAND_SPLIT_LIMIT = 2;

    public CommandContext {
        arguments = List.copyOf(arguments); // Inmutable defensivo
    }

    /**
     * Parsea la entrada de texto plano.
     * <p>
     * Ejemplo: "/telep juan 1 50 50" -> command="/telep", arguments=["juan", "1", "50", "50"]
     */
    public static CommandContext parse(String text) {
        String trimmedText = text.trim();
        String[] commandParts = splitCommandFromArguments(trimmedText);

        String command = commandParts[0];
        String argumentsRaw = extractArgumentsRaw(commandParts);
        List<String> arguments = parseArgumentsList(argumentsRaw);

        return new CommandContext(trimmedText, command, arguments, argumentsRaw);
    }

    public boolean isCommand() {
        return command.startsWith("/");
    }

    public boolean isYell() {
        return command.startsWith("-");
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    public int getArgumentCount() {
        return arguments.size();
    }

    public String getArgument(int index) {
        return index >= 0 && index < arguments.size() ? arguments.get(index) : "";
    }

    public String getText() {
        return isYell() ? text.substring(1) : text;
    }

    private static String[] splitCommandFromArguments(String text) {
        return text.split("\\s+", COMMAND_SPLIT_LIMIT);
    }

    private static String extractArgumentsRaw(String[] commandParts) {
        return commandParts.length > 1 ? commandParts[1] : "";
    }

    private static List<String> parseArgumentsList(String argumentsRaw) {
        return argumentsRaw.isEmpty() ? List.of() : List.of(argumentsRaw.split("\\s+"));
    }

}