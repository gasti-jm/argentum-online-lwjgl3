package org.aoclient.network.protocol.command.core;

import java.util.List;

/**
 * Clase que representa el contexto de un comando. Contiene el comando principal, una lista de argumentos individuales, y los
 * argumentos en su formato original ("en crudo").
 * <p>
 * Es utilizada para analizar y descomponer comandos en sus partes principales, permitiendo una manipulacion sencilla del comando
 * y sus argumentos.
 */

public record CommandContext(
        String command,
        List<String> arguments, // Para comandos que necesitan la lista de argumentos
        String argumentsRaw // Para comandos que necesitan el texto completo ("los argumentos en crudo")
) {

    /** Limite para controlar la cantidad de veces que se divide un comando en sus partes (comando y argumentos). */
    private static final int COMMAND_SPLIT_LIMIT = 2;

    public CommandContext {
        arguments = List.copyOf(arguments); // Inmutable defensivo
    }

    /**
     * Parsea un comando en una estructura que contiene el comando principal, una lista de argumentos y los argumentos en su
     * formato original.
     *
     * @param commandInput cadena de texto que representa el comando completo ingresado, incluyendo el comando principal y los
     *                     posibles argumentos
     * @return un objeto {@code CommandContext} que contiene el comando principal, los argumentos como lista y los argumentos en
     * formato "en crudo"
     */
    public static CommandContext parse(String commandInput) {
        /* Convierte la entrada de la cadena a minusculas en caso de que el usuario haya especificado el comando en mayusculas, y
         * luego recorta los posibles espacios iniciales y finales del comando. */
        String trimmedCommand = commandInput.toLowerCase().trim();
        String[] commandParts = splitCommand(trimmedCommand);

        String command = commandParts[0];
        String argumentsRaw = extractArgumentsRaw(commandParts);
        List<String> arguments = parseArgumentsList(argumentsRaw);

        return new CommandContext(command, arguments, argumentsRaw);
    }

    public boolean isCommand() {
        return command.startsWith("/");
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    public int getArgumentCount() {
        return arguments.size();
    }

    /**
     * Obtiene el argumento en la posicion especificada dentro de la lista de argumentos.
     *
     * @param index indice del argumento
     * @return el argumento correspondiente al indice proporcionado como una cadena, o una cadena vacia si el indice no es valido
     */
    public String getArgument(int index) {
        return index >= 0 && index < arguments.size() ? arguments.get(index) : "";
    }

    /**
     * Divide un comando en dos partes: el comando principal y un posible conjunto de argumentos.
     * <p>
     * La division se realiza basandose en espacios y se limita a dos partes como maximo.
     *
     * @param trimmedCommand Cadena del comando ya recortada (sin espacios en blanco al inicio ni al final). Esta cadena
     *                       representa el comando completo que se desea dividir.
     * @return un arreglo de cadenas donde la primera posicion contiene el comando principal y la segunda, si existe, contiene el
     * resto de los argumentos como un texto completo
     */
    private static String[] splitCommand(String trimmedCommand) {
        return trimmedCommand.split("\\s+", COMMAND_SPLIT_LIMIT);
    }

    /**
     * Extrae la parte de los argumentos "en crudo" desde un comando ya dividido en sus partes. Si el comando contiene al menos
     * dos partes, devuelve la segunda parte que corresponde a los argumentos como una cadena de texto completa. Si no hay una
     * segunda parte, devuelve una cadena vacia.
     *
     * @param commandParts Arreglo de cadenas donde cada elemento representa una parte del comando que ya ha sido dividido. Se
     *                     espera que la primera parte sea el comando principal y, si existe, la segunda contenga los argumentos
     *                     en crudo.
     * @return una cadena que representa los argumentos en crudo si existen; de lo contrario, devuelve una cadena vacia
     */
    private static String extractArgumentsRaw(String[] commandParts) {
        return commandParts.length > 1 ? commandParts[1] : "";
    }

    /**
     * Convierte una cadena de texto que contiene argumentos en una lista de argumentos individuales.
     * <p>
     * La cadena de entrada se divide utilizando espacios como separadores. Si la cadena no contiene ningun argumento (es vacia),
     * se devuelve una lista vacia.
     *
     * @param argumentsRaw Cadena de texto que representa los argumentos en formato "en crudo". Esta cadena puede estar vacia o
     *                     contener multiples argumentos separados por espacios.
     * @return Una lista que contiene cada argumento como un elemento separado. Si la cadena ingresada esta vacia, se devuelve una
     * lista vacia.
     */
    private static List<String> parseArgumentsList(String argumentsRaw) {
        return argumentsRaw.isEmpty() ? List.of() : List.of(argumentsRaw.split("\\s+"));
    }

}